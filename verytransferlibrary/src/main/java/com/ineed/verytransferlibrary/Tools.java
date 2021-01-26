package com.ineed.verytransferlibrary;

import java.util.Date;

public class Tools {

    public static boolean TransferChecker(String sms_address,
                                          String sms_body,
                                          String sms_type,
                                          String sms_date,
                                          Double amount_desired,
                                          String number_benefactor,
                                          Boolean exact,
                                          Integer pay_days) {
        try {
            boolean isCubacel = sms_address.equals("Cubacel");
            boolean startCorrect = sms_body.startsWith("Usted ha transferido");
            boolean containsWords = sms_body.contains("al numero")
                    && sms_body.contains("Saldo principal")
                    && sms_body.contains("linea activa hasta")
                    && sms_body.contains("vence");
            String[] numbers = getNumbersDecimals(sms_body).split("-");
            boolean isNumber = numbers[1].equals(number_benefactor);
            Double amount = Double.valueOf(numbers[0]);
            boolean isAmount;
            if (exact) {
                isAmount = (amount.equals(amount_desired));
            } else {
                isAmount = (amount >= amount_desired);
            }
            boolean is_in_time = false;
            if ((Long.parseLong(sms_date) + ((long) (1000 * 60 * 60 * 24) * pay_days)) >= new Date().getTime()
                    && Long.parseLong(sms_date) <= new Date().getTime()) {
                is_in_time = true;
            }
            boolean isReceived = sms_type.equals("1");
            return isAmount && isCubacel && isNumber && startCorrect && isReceived && containsWords && is_in_time;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getNumbersDecimals(String s) {
        String result = getNumbersOfString(s);
        String[] f = result.split("-");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < f.length; i++) {
            sb.append(trimPoints(f[i]));
            sb.append("-");
        }
        return sb.toString();
    }

    public static String trimPoints(String s) {
        if (s.startsWith(".")) {
            s = s.substring(1);
        }
        if (s.endsWith(".")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public static String getNumbersOfString(String s) {
        boolean aux = false;
        String[] n = s.split("");
        StringBuffer f = new StringBuffer();
        for (int i = 0; i < n.length; i++) {
            if (n[i].matches("[0-9]") || n[i].equals(".")) {
                f.append(n[i]);
                aux = true;
            } else {
                if (aux) {
                    f.append("-");
                }
                aux = false;
            }
        }
        return f.toString();
    }
}
