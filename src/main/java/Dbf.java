import com.linuxense.javadbf.DBFDataType;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFWriter;
import util.ExcelData;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.*;


public class Dbf {
    private TextArea textArea;

    public Dbf(TextArea textArea) {
        this.textArea = textArea;
    }

    public Dbf() {

    }

    ;

    /**
     * D:\test\两融产品发送清单0329.xlsx
     * D:\test\rzrq_dzls_20220325.dbf
     * D:\test
     *
     * @param readFile
     * @param writePath
     */
    public void handler(String customerFile, String readFile, String writePath) {
        if (customerFile == null || "".equals(customerFile.trim()) || readFile == null || "".equals(readFile.trim()) || writePath == null || "".equals(writePath.trim())) {
            System.out.println("入参为空");
            if (textArea != null) textArea.append("入参为空\r\n");
            return;
        }

        Record record = readDBF(readFile);
        writeDBF(customerFile, record, writePath);
    }

    /**
     * 读取dbf
     *
     * @param readFile
     */
    private Record readDBF(String readFile) {
        Record record = new Record();

        File file = new File(readFile);
        record.fileName = file.getName();
        InputStream fis = null;
        try {
            // 读取文件的输入流
            fis = new FileInputStream(readFile);
            // 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息
            DBFReader reader = new DBFReader(fis);
            // 解决乱码
            reader.setCharactersetName("GBK");
            // 调用DBFReader对实例方法得到path文件中字段的个数
            int fieldsCount = reader.getFieldCount();
            // 取出字段信息
            record.dBFFields = new DBFField[fieldsCount];
            for (int i = 0; i < fieldsCount; i++) {
                DBFField field = reader.getField(i);
                record.dBFFields[i] = field;
                System.out.println(field.getName());
                if (textArea != null) textArea.append(field.getName() + "\r\n");
            }

            Object[] rowValues;
            // 一条条取出path文件中记录
            record.customerNoRecordMap = new LinkedHashMap<>();
            while ((rowValues = reader.nextRecord()) != null) {
                StringBuilder recordRowSb = new StringBuilder();
                for (int i = 0; i < rowValues.length; i++) {
                    recordRowSb.append(rowValues[i]);
                    recordRowSb.append("@");
                    System.out.println(rowValues[i]);
                    if (textArea != null) textArea.append(String.valueOf(rowValues[i]) + "\r\n");
                }

                String recordRow = recordRowSb.substring(0, recordRowSb.length() - 1);
                if (record.customerNoRecordMap.containsKey((String) rowValues[1])) {
                    List<String> recordList = record.customerNoRecordMap.get((String) rowValues[1]);
                    recordList.add(recordRow);
                } else {
                    ArrayList<String> recordList = new ArrayList<>();
                    recordList.add(recordRow);
                    record.customerNoRecordMap.put((String) rowValues[1], recordList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (textArea != null) textArea.append(e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
            }
        }

        return record;
    }

    /**
     * 写dbf
     *
     * @param writePath
     */
    private void writeDBF(String customerFile, Record record, String writePath) {
        LinkedHashMap<String, String> productAccountMap = new LinkedHashMap<>();
        try {
            ExcelData sheet1 = new ExcelData(customerFile, "sheet1");
            int physicalNumberOfRows = sheet1.getPhysicalNumberOfRows();
            for (int i = 1; i < physicalNumberOfRows; i++) {
                String product = sheet1.getExcelDateByIndex(i, 0);
                String account = sheet1.getExcelDateByIndex(i, 1);
                productAccountMap.put(product, account);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (textArea != null) textArea.append(e.getMessage());
            return;
        }

        OutputStream fos = null;
        try {

            for (Map.Entry<String, String> entry : productAccountMap.entrySet()) {
                DBFWriter writer = new DBFWriter();
                // 把字段信息写入DBFWriter实例，即定义表结构
                writer.setFields(record.dBFFields);

                String date = record.fileName.substring(record.fileName.length() - 12, record.fileName.length() - 4);
                String pathDir = writePath + "\\" + date + "\\" + entry.getKey() + date;
                File path = new File(pathDir);
                if (!path.exists()) {
                    boolean success = path.mkdirs();
                    if (!success) {
                        throw new Exception("输出目录创建失败" + pathDir);
                    }
                }

                String fileName = record.fileName.replace(".dbf", "");
                String file = path + "\\" + fileName + ".dbf";
                List<String> recordRowList = record.customerNoRecordMap.get(entry.getValue());
                if (recordRowList != null && recordRowList.size() != 0) {

                    for (String recordRow : recordRowList) {
                        String[] columnStringValues = recordRow.split("@", -1);
                        Object[] columnObjectValues = new Object[columnStringValues.length];
                        for (int i = 0; i < columnStringValues.length; i++) {
                            switch (DBFDataType.fromCode(record.dBFFields[i].getDataType())) {
                                case CHARACTER:
                                    columnObjectValues[i] = columnStringValues[i];
                                    break;
                                case LOGICAL:
                                    columnObjectValues[i] = "null".equals(columnStringValues[i]) ? null : new Boolean(columnStringValues[i]);
                                    break;
                                case DATE:
                                    columnObjectValues[i] = "null".equals(columnStringValues[i]) ? null : new Date(columnStringValues[i]);
                                    break;
                                case NUMERIC:
                                    columnObjectValues[i] = "null".equals(columnStringValues[i]) ? null : new BigDecimal(columnStringValues[i]);
                                    break;
                                case FLOATING_POINT:
                                    columnObjectValues[i] = "null".equals(columnStringValues[i]) ? null : new BigDecimal(columnStringValues[i]);
                                    break;
                                default:
                                    columnObjectValues[i] = columnStringValues[i];
                            }
                        }

                        writer.addRecord(columnObjectValues);
                    }
                }

                // 定义输出流，并关联的一个文件
                fos = new FileOutputStream(file);
                // 写入数据
                writer.write(fos);
                // writer.write();

                fos.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
            if (textArea != null) textArea.append(e.getMessage());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
            }
        }
    }

    static class Record {
        String fileName;
        DBFField[] dBFFields;
        Map<String, List<String>> customerNoRecordMap;
    }

}
