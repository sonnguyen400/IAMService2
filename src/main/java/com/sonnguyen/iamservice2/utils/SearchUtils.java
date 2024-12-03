package com.sonnguyen.iamservice2.utils;

import com.sonnguyen.iamservice2.specification.AccountSpecification;
import com.sonnguyen.iamservice2.specification.DynamicSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
public class SearchUtils {
    public static List<DynamicSearch> parseOperator(Map<String,String[]> parameterMap) {
        List<DynamicSearch> searchItem = new ArrayList<>();
        for(Map.Entry<String,String[]> set : parameterMap.entrySet()) {
            if(set.getKey().equals("page")||set.getKey().equals("size")||set.getKey().equals("order")) continue;
            for(String value:set.getValue()){
                String[] extractOperatorValue=value.split("[()]");
                if(extractOperatorValue.length==2){
                    String searchValue=extractOperatorValue[1];
                    DynamicSearch.Operator operator=DynamicSearch.Operator.valueOf(extractOperatorValue[0].toUpperCase());
                    searchItem.add(new DynamicSearch(set.getKey(),searchValue,operator));
                    log.info("key: {} value: {} operator: {}",set.getKey(),searchValue,operator);
                }
            }
        }
        parameterMap.forEach((key,values)->{


        });
        return searchItem;
    }
    public static List<Sort.Order> parseSort(Map<String,String[]> parameterMap) {
        List<Sort.Order> sortItem = new ArrayList<>();
        String[] ordersParam=parameterMap.get("order");
        if(ordersParam.length<1) return List.of();
        for(String param:ordersParam) {
            String[] extractOrder=param.split("[()]");
            if(extractOrder.length==2){
                String field=extractOrder[1];
                log.info("Sort by {} direction: {}",field,extractOrder[0]);
                Sort.Direction direction=Sort.Direction.valueOf(extractOrder[0]);
                sortItem.add(new Sort.Order(direction,field));
            }else throw new InvalidParameterException(String.format("Invalid sort parameter near %s",param));
        }

        return sortItem;
    }
}
