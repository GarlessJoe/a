package com.joe.security.custom;

import com.joe.common_.MD5.MD5;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
public class customMD5Encoding implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        String pwdAfter = MD5.encrypt(charSequence.toString());
        return pwdAfter;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        System.out.println(s.equals(encode(charSequence.toString())));
      return s.equals(encode(charSequence.toString()));
    }
}
