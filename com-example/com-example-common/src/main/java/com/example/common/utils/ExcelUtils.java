package com.example.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @PackagePath com.example.common.utils.ExcelUtils
 * @Author YINZHIYU
 * @Date 2020/5/22 11:56
 * @Version 1.0.0.0
 **/
@Component
public class ExcelUtils<T> {
    private static Map<Field, String> filedMap = Maps.newLinkedHashMap();

    public HSSFWorkbook parseToHSSFWorkbook(List<T> list) {

        // 创建excel
        HSSFWorkbook wk = new HSSFWorkbook();
        // 创建一张工作表
        HSSFSheet sheet = wk.createSheet();
        // 2
        sheet.setColumnWidth(0, 5000);

        int rowIndex = 0;

        HSSFRow row = sheet.createRow(rowIndex);
        buildColumn(row, null);
        for (T vo : list) {
            rowIndex++;
            row = sheet.createRow(rowIndex);
            buildColumn(row, vo);
        }

        return wk;
    }

    private void buildColumn(HSSFRow row, T vo) {
        int columnIndex = 0;
        for (Field field : filedMap.keySet()) {
            HSSFCell cell = row.createCell(columnIndex);
            try {
                String value;
                if (ObjectUtil.isNotNull(vo)) {
                    value = String.valueOf(field.get(vo));
                } else {
                    value = filedMap.get(field);
                }
                if (ObjectUtil.isNotNull(value)) {
                    cell.setCellValue(value);
                }
                columnIndex++;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
