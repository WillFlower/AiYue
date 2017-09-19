package com.wgh.aiyue.greendao;

import com.wgh.aiyue.util.ConstDefine;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by   : WGH.
 */
public class ListConverter implements PropertyConverter<ArrayList<String>, String> {

    @Override
    public ArrayList<String> convertToEntityProperty(String databaseValue) {
        ArrayList<String> arrayList;
        if (databaseValue == null) {
            return null;
        } else {
            List<String> list = Arrays.asList(databaseValue.split(ConstDefine.SplitString));
            arrayList = new ArrayList<>(list);
            return arrayList;
        }
    }

    @Override
    public String convertToDatabaseValue(ArrayList<String> entityProperty) {
        if (entityProperty == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            for (String entity : entityProperty) {
                sb.append(entity);
                sb.append(ConstDefine.SplitString);
            }
            return sb.toString();
        }
    }
}
