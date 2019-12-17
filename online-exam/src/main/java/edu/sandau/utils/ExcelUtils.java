package edu.sandau.utils;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.apache.poi.ss.usermodel.CellType.STRING;

public class ExcelUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);
    /**
     * 要求excel版本在2007以上
     *
     * @param file 文件信息
     * @return
     * @throws Exception
     */
    public static List<List<Object>> readExcel(File file) throws Exception {
        if(!file.exists()){
            throw new Exception("找不到文件");
        }
        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
        // 读取第一张表格内容
        XSSFSheet sheet = xwb.getSheetAt(0);
        return analysisExcel(sheet);
    }

    /**
     * 要求excel版本在2007以上
     *
     * @param inputStream 文件信息
     * @return
     * @throws Exception
     */
    public static List<List<Object>> readExcel(InputStream inputStream) throws Exception {
        XSSFWorkbook xwb = new XSSFWorkbook(inputStream);
        // 读取第一张表格内容
        XSSFSheet sheet = xwb.getSheetAt(0);
        if ( sheet.getPhysicalNumberOfRows() == 0 ) {
            return null; //若该表为空，返回空
        }
        return analysisExcel(sheet);
    }

    private static List<List<Object>> analysisExcel(XSSFSheet sheet) {
        List<List<Object>> list = new LinkedList<List<Object>>();
        XSSFRow row = null;

        //获取列的标题
        row = sheet.getRow(0);
        List<Object> titleLinked = new LinkedList<>();
        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
            Object value = getValue(row, j);
            if ( value == null ) {
                continue;
            }
            titleLinked.add(value);
        }
        list.add(titleLinked);

        //获取列的内容
        for (int i = (sheet.getFirstRowNum() + 1); i <= (sheet.getPhysicalNumberOfRows() - 1); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<Object> valueLinked = new LinkedList<>();
            for (int j = row.getFirstCellNum(); j < titleLinked.size(); j++) {
                Object value = getValue(row, j);
                if ( value == null ) {
                    value = "";
                }
                valueLinked.add(value);
            }

            //不添加空着的行
            if ( valueLinked.size() != 0) {
                boolean flag = true;
                int cont = 0;
                for ( Object v: valueLinked ) {
                    if ( v == null || v.equals("") ) {
                        cont++;
                    }
                }
                if ( cont == valueLinked.size() ) {
                    flag = false;
                }
                if ( flag ) {
                    list.add(valueLinked);
                }
            }
        }
        return list;
    }

    private static Object getValue(XSSFRow row, int j) {
        Object value = null;
        XSSFCell cell = null;
        cell = row.getCell(j);
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                //String类型返回String数据
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                //日期数据返回LONG类型的时间戳
                if ("yyyy\"年\"m\"月\"d\"日\";@".equals(cell.getCellStyle().getDataFormatString())) {
                    Date javaDate = DateUtil.getJavaDate(cell.getNumericCellValue());
                    value = javaDate.getTime() / 1000;
                } else {
                    //数值类型返回double类型的数字
//                    LOGGER.info(cell.getNumericCellValue()+":格式："+cell.getCellStyle().getDataFormatString());
                    value = cell.getNumericCellValue();
                }
                break;
            case BOOLEAN:
                //布尔类型
                value = cell.getBooleanCellValue();
                break;
            case BLANK:
                //空单元格
                value = "";
                break;
            default:
                value = cell.toString();
        }
        return value;
    }

    /**
     * 导出excel
     * @param excel_name 导出的excel路径（需要带.xlsx)
     * @param headList  excel的标题备注名称
     * @param fieldList excel的标题字段（与数据中map中键值对应）
     * @param dataList  excel数据
     * @throws Exception
     */
    public static void createExcel(String excel_name, String[] headList,
                                   String[] fieldList, List<Map<String, Object>> dataList)
            throws Exception {
        // 创建新的Excel 工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 在Excel工作簿中建一工作表，其名为缺省值
        XSSFSheet sheet = workbook.createSheet();
        // 在索引0的位置创建行（最顶端的行）
        XSSFRow row = sheet.createRow(0);
        // 设置excel头（第一行）的头名称
        for (int i = 0; i < headList.length; i++) {

            // 在索引0的位置创建单元格（左上端）
            XSSFCell cell = row.createCell(i);
            // 定义单元格为字符串类型
            cell.setCellType(STRING);
            // 在单元格中输入一些内容
            cell.setCellValue(headList[i]);
        }
        // ===============================================================
        //添加数据
        for (int n = 0; n < dataList.size(); n++) {
            // 在索引1的位置创建行（最顶端的行）
            XSSFRow row_value = sheet.createRow(n + 1);
            Map<String, Object> dataMap = dataList.get(n);
            // ===============================================================
            for (int i = 0; i < fieldList.length; i++) {

                // 在索引0的位置创建单元格（左上端）
                XSSFCell cell = row_value.createCell(i);
                // 定义单元格为字符串类型
                cell.setCellType(STRING);
                // 在单元格中输入一些内容
                cell.setCellValue((dataMap.get(fieldList[i])).toString());
            }
            // ===============================================================
        }
        // 新建一输出文件流
        FileOutputStream fos = new FileOutputStream(excel_name);
        // 把相应的Excel 工作簿存盘
        workbook.write(fos);
        fos.flush();
        // 操作结束，关闭文件
        fos.close();
    }
}