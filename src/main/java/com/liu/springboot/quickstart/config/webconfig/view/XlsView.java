package com.liu.springboot.quickstart.config.webconfig.view;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.liu.springboot.quickstart.bean.DemoObj;

/**
 * excel view
 */
public class XlsView extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        try {
            DemoObj demoObj = (DemoObj) model.get("demoObj");
            if(demoObj == null) {
                logger.warn("-----在返回的Model中没有找到要解析的指定变量");
                return;
            }
            Sheet sheet = workbook.createSheet("sheet1");
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setAlignment(CellStyle.ALIGN_CENTER);
            Row row = null;
            Cell cell = null;
            int rowCount = 0;
            int colCount = 0;

            // 创建头部
            row = sheet.createRow(rowCount++);

            //通过反射获得类中所有的字段名，并将其设置为头信息
            Field[] fields = demoObj.getClass().getDeclaredFields();
            String[] names = new String[fields.length];
            Object[] values = new Object[fields.length];
            Field.setAccessible(fields,   true);
            for (int i=0; i<names.length; i++) {  
                String name = fields[i].getName();
                names[i] = name;
                values[i] = fields[i].get(demoObj);
                cell = row.createCell(colCount++);
                cell.setCellStyle(style);
                cell.setCellValue(name);
            }

//            cell = row.createCell(colCount++);
//            cell.setCellStyle(style);
//            cell.setCellValue("NAME");


            // 创建数据
            row = sheet.createRow(rowCount++);
            colCount = 0;
            for(Object a : values) {
                row.createCell(colCount++).setCellValue(String.valueOf(a));
            }
//            row.createCell(colCount++).setCellValue(demoObj.getId());
//            row.createCell(colCount++).setCellValue(demoObj.getName());
            for (int i = 0; i < 3; i++)
                sheet.autoSizeColumn(i, true);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("-----解析excel失败！！！-----");
            throw e;
        }
    }

}
