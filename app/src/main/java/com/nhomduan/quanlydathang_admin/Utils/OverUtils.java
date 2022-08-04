package com.nhomduan.quanlydathang_admin.Utils;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.nhomduan.quanlydathang_admin.model.Product;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OverUtils {
//    public static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
//    public static NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
    public static final String HOAT_DONG = "HOAT_DONG";
    public static final String DUNG_KINH_DOANH = "DUNG_KINH_DOANH";
    public static final String HET_HANG = "HET_HANG";
    public static final String SAP_RA_MAT = "SAP_RA_MAT";


    public static final String PASS_LOGIN_ACTIVITY = "PASS_LOGIN";
    public static final String NO_PASS = "NO_PASS";
    public static final String PASS_MAIN_ACTIVITY = "PASS_MAIN_ACTIVITY";
    public static final String PASS_FILE = "PASS_FILE";

    private static SharedPreferences sharedPreferences;

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm * dd/MM/yyyy");
    private static Locale locale = new Locale("vi", "VN");
    public static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
    public static final String ERROR_MESSAGE = "Lỗi thực hiện";


    public static void makeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }//

    public static SharedPreferences getSPInstance(Context context, String nameFile) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(nameFile, MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static String getExtensionFile(Context context, Uri uri) {
        ContentResolver cr = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public static List<Product> filterProduct(List<Product> products) {
        List<Product> result = new ArrayList<>();
        for(Product product : products) {
            if(product.getTrang_thai().equals(HOAT_DONG)) {
                result.add(product);
            }
        }
        return result;
    }

    public static List<Product> filterProduct2(List<Product> products) {
        List<Product> result = new ArrayList<>();
        for(Product product : products) {
            if(product.getTrang_thai().equals(HOAT_DONG) || product.getTrang_thai().equals(SAP_RA_MAT)) {
                result.add(product);
            }
        }
        return result;
    }


    public static List<Product> filterProduct3(List<Product> resultList) {
        List<Product> result = new ArrayList<>();
        for(Product product : resultList) {
            if(product.getSo_luong_da_ban() > 0) {
                result.add(product);
            }
        }
        return result;
    }
}
