package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.exception.DuplicatedException;
import com.sonnguyen.iamservice2.exception.ResourceNotFoundException;
import com.sonnguyen.iamservice2.exception.WorkbookValidationException;
import com.sonnguyen.iamservice2.model.Account;
import com.sonnguyen.iamservice2.repository.AccountRepository;
import com.sonnguyen.iamservice2.specification.AccountSpecification;
import com.sonnguyen.iamservice2.specification.DynamicSearch;
import com.sonnguyen.iamservice2.viewmodel.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    PrivateStorageService privateStorageService;
    PublicStorageService publicStorageService;
    ValidatorFactory validatorFactory;
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public UserDetailGetVm findAccountDetailById(Long id) {
        return accountRepository.findById(id)
                .map(UserDetailGetVm::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public Optional<Account> findByEmail(String username) {
        return accountRepository.findByEmail(username);
    }

    @Override
    public void register(UserRegistrationPostVm userRegistrationPostVm) {
        Account account = userRegistrationPostVm.toEntity();
        account.setVerified(false);
        saveAccount(account);
    }

    @Override
    @Transactional
    public void updateLockedStatusByEmail(Boolean isLocked, String email) {
        accountRepository.updateAccountLockStatusByEmail(isLocked, email);
    }

    @Override
    public void create(UserCreationPostVm userCreationPostVm) {
        Account account = userCreationPostVm.toEntity();
        saveAccount(account);
    }

    @Transactional
    public void verifyAccountByEmail(String email) {
        accountRepository.verifiedAccountByEmail(email);
    }

    public Account saveAccount(Account account) {
        if (accountRepository.existsAccountByEmail(account.getEmail())) {
            throw new DuplicatedException("Email was registered");
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteByEmail(String email) {
        if (existedByEmail(email)) {
            accountRepository.softDeleteByEmail(email);
        }
        return ResponseEntity.status(Response.Status.NOT_FOUND.getStatusCode())
                .body("User registered with email" + email + "not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteById(Object id) {
        accountRepository.softDeleteById((Long) id);
        return ResponseEntity.ok().build();
    }

    public boolean existedByEmail(String email) {
        return accountRepository.existsAccountByEmail(email);
    }

    public Page<UserDetailGetVm> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable).map(UserDetailGetVm::fromEntity);
    }

    public Page<Account> findAllAccounts(List<AccountSpecification> specifications, Pageable pageable){
        Specification<Account> accountSpecification=combineSpecification(specifications);
        return accountRepository.findAll(accountSpecification,pageable);
    }

    public Page<UserDetailGetVm> findAll(List<AccountSpecification> specifications, Pageable pageable) {
       return findAllAccounts(specifications,pageable).map(UserDetailGetVm::fromEntity);
    }

    @Override
    @Transactional
    public void resetPasswordByAccountId(Long accountId, String rawPassword) {
        String password = passwordEncoder.encode(rawPassword);
        accountRepository.resetPasswordByAccountId(accountId, password);
    }


    public void updateAccountProfileById(Long accountId, UserProfilePostVm userProfilePostVm) {
        Account oldAccount = findById(accountId);
        Account newAccount = mapNewAccountProfile(oldAccount, userProfilePostVm);
        accountRepository.save(newAccount);
    }

    private Account mapNewAccountProfile(Account oldAccount, UserProfilePostVm userProfilePostVm) {
        oldAccount.setFirstName(userProfilePostVm.firstname());
        oldAccount.setLastName(userProfilePostVm.lastname());
        oldAccount.setAddress(userProfilePostVm.address());
        oldAccount.setPhone(userProfilePostVm.phone());
        oldAccount.setDateOfBirth(userProfilePostVm.dateOfBirth());
        return oldAccount;
    }

    @Transactional
    @Override
    public void updatePasswordByEmail(ChangePasswordPostVm changePasswordPostVm) {
        String encodedPassword = passwordEncoder.encode(changePasswordPostVm.newPassword());
        accountRepository.updatePasswordByEmail(changePasswordPostVm.email(), encodedPassword);
    }

    public void exportAccountsToExcel(List<AccountSpecification> specifications, HttpServletResponse response){
        Specification<Account> accountSpecification=combineSpecification(specifications);
        List<Account> accounts=accountRepository.findAll(accountSpecification);
        try (Workbook workbook=parseAccountToWorkbook(accounts)){
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Workbook parseAccountToWorkbook(List<Account> accounts) throws IOException {
        Workbook wb=null;
        try(InputStream fileInputStream= Files.newInputStream(Paths.get("src/main/resources/accounts_sample.xlsx"))){
            wb= WorkbookFactory.create(fileInputStream);
        } catch (IOException e) {
            wb=new XSSFWorkbook();
        }
        Sheet sheet=wb.getSheetAt(0);
        CellStyle style=wb.createCellStyle();
        Font font=wb.createFont();
        font.setFontName("Times New Roman");
        style.setFont(font);
        for(int i=0;i<accounts.size();i++){
            Row row=sheet.createRow(i+1);
            row.setRowStyle(style);
            setAccountValue(i+1,row,accounts.get(i),style);
        }
        return wb;
    }
    public static void setAccountValue(int index,Row row,Account account,CellStyle style){
        Cell cell0=row.createCell(0);
        cell0.setCellValue(index);
        cell0.setCellStyle(style);
        Cell cell1=row.createCell(1);
        cell1.setCellStyle(style);
        cell1.setCellValue(account.getEmail());
        Cell cell2=row.createCell(2);
        cell2.setCellStyle(style);
        cell2.setCellValue(account.getFirstName());
        Cell cell3=row.createCell(3);
        cell3.setCellStyle(style);
        cell3.setCellValue(account.getLastName());
        Cell cell4=row.createCell(4);
        cell4.setCellStyle(style);
        cell4.setCellValue(new SimpleDateFormat("yyyy/MM/dd").format(account.getDateOfBirth()));
        Cell cell5=row.createCell(5);
        cell5.setCellStyle(style);
        cell5.setCellValue(account.getStreet());
        Cell cell6=row.createCell(6);
        cell6.setCellStyle(style);
        cell6.setCellValue(account.getCommune());
        Cell cell7=row.createCell(7);
        cell7.setCellStyle(style);
        cell7.setCellValue(account.getDistrict());
        Cell cell8=row.createCell(8);
        cell8.setCellStyle(style);
        cell8.setCellValue(account.getProvince());
        Cell cell9=row.createCell(9);
        cell9.setCellStyle(style);
        cell9.setCellValue(account.getYearOfExperience());
    }

    public Specification<Account> combineSpecification(List<AccountSpecification> specifications){
        Specification<Account> combined = new AccountSpecification(new DynamicSearch("deleted", "false", DynamicSearch.Operator.EQUAL));
        for (AccountSpecification specification : specifications) {
            combined = combined.and(specification);
        }
        return combined;
    }

    public void importAccountsFromExcel(MultipartFile file) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String owner="anonymous";
        if(authentication!=null) owner= (String) authentication.getPrincipal();
        try(XSSFWorkbook workbook=new XSSFWorkbook(file.getInputStream())) {
            List<Account> accounts=parseAndValidateAccountsFromExcel(workbook);
            accountRepository.saveAll(accounts);
            privateStorageService.uploadAllFile(List.of(file),owner);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public  List<Account> parseAndValidateAccountsFromExcel(Workbook workbook) throws ParseException {
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Validator validator = validatorFactory.getValidator();
        Sheet sheet=workbook.getSheetAt(0);
        Iterator<Row> rows=sheet.iterator();
        List<Account> accounts=new ArrayList<>();
        List<WorkbookValidationMessage> workbookValidationMessageList=new ArrayList<>();
        rows.next();
        int colIndex=0;
        while (rows.hasNext()){
            Row row=rows.next();
            colIndex=1;
            try{
                String email=row.getCell(1).getStringCellValue();colIndex++;
                String firstName=row.getCell(2).getStringCellValue();colIndex++;
                String lastName=row.getCell(3).getStringCellValue();colIndex++;
                Date dateOfBirth=dateFormat.parse(row.getCell(4).getStringCellValue());colIndex++;
                String street=row.getCell(5).getStringCellValue();colIndex++;
                String commune=row.getCell(6).getStringCellValue();colIndex++;
                String district=row.getCell(7).getStringCellValue();colIndex++;
                String province=row.getCell(8).getStringCellValue();colIndex++;
                double experience=row.getCell(9).getNumericCellValue();colIndex++;
                Account account=Account.builder()
                        .email(email)
                        .firstName(firstName)
                        .lastName(lastName)
                        .dateOfBirth(dateOfBirth)
                        .street(street)
                        .commune(commune)
                        .district(district)
                        .province(province)
                        .yearOfExperience((int) experience)
                        .verified(true)
                        .deleted(false)
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .build();
                Set<ConstraintViolation<Account>> violations = validator.validate(account);
                if (!violations.isEmpty()) {
                    List<String> violationMessages=violations.stream().map(violation_->violation_.getPropertyPath().toString()+" "+violation_.getMessage()).toList();
                    workbookValidationMessageList.add(WorkbookValidationMessage.builder()
                            .message(violationMessages)
                            .row(row.getRowNum()).build());
                }
                if(existedByEmail(account.getEmail())){
                    workbookValidationMessageList.add(WorkbookValidationMessage.builder()
                            .message(List.of("Duplicated email"))
                            .row(row.getRowNum()).build());
                }
                accounts.add(account);
            } catch (Exception e) {
                workbookValidationMessageList.add(WorkbookValidationMessage.builder()
                        .row(row.getRowNum())
                        .column(colIndex)
                        .message(List.of(e.getMessage())).build());
            }
        }
        if(!workbookValidationMessageList.isEmpty()){
            throw new WorkbookValidationException(workbookValidationMessageList);
        }
        return accounts;
    }
    @Transactional
    public void updateProfilePictureByEmail(MultipartFile file,String email){
        Account account=findByEmail(email).orElseThrow(()->new RuntimeException("Account not found"));
        ResponseEntity<List<FileUploadedResponseVm>> uploadedResponse= (ResponseEntity<List<FileUploadedResponseVm>>) publicStorageService.uploadAllFile(List.of(file),email);
        String link= Objects.requireNonNull(uploadedResponse.getBody()).getFirst().link();
        accountRepository.updateAccountPictureByAccountID(account.getId(),link);

    }
}
