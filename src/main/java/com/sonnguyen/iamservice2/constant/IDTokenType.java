package com.sonnguyen.iamservice2.constant;

public enum IDTokenType {
    VERIFY_ACCOUNT("verify_account"),
    FORGOT_PASSWORD("forgot_password"),
    ACCEPT_LOGIN("accept_login"),
    ;
    public final String value;

    IDTokenType(String value) {
        this.value = value;
    }
}
