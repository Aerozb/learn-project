package a;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 导出excel表格
 * 第一个sheet为折线图的图片
 * 第二个sheet为数据，list类型的成一列排开
 */
public class MultiSpectrumGraphToExcel {

    private static final int CHART_WIDTH = 800;
    private static final int CHART_HEIGHT = 600;

    public static void main(String[] args) {
        try {
            List<SpectrumGraphDataDto> graphDataList1 = createGraphDataList(
                    Arrays.asList("400", "450", "500", "550", "600"),
                    Arrays.asList("0.2", "0.5", "0.8", "0.6", "0.3"),
                    Arrays.asList("0.1", "0.4", "0.7", "0.5", "0.2")
            );

            WaterDataRawExportDto waterDataRawExportDto1 = createWaterDataRawExportDto(
                    "DeviceID1", "Enterprise1", new Date(),
                    "TemperatureSensor1", "TemperatureProbe1", "Humidity1", "Intensity1",
                    Arrays.asList("400", "450", "500", "550", "600"),
                    Arrays.asList("0.1", "0.2", "0.3", "0.4", "0.5"),
                    Arrays.asList("0.5", "0.4", "0.3", "0.2", "0.1"),
                    Arrays.asList("0.2", "0.3", "0.4", "0.5", "0.6"),
                    Arrays.asList("0.6", "0.5", "0.4", "0.3", "0.2")
            );

            List<SpectrumGraphDataDto> graphDataList2 = createGraphDataList(
                    Arrays.asList("1400", "1450", "500", "550", "600"),
                    Arrays.asList("3", "6", "9", "7", "4"),
                    Arrays.asList("2", "5", "8", "6", "3")
            );

            WaterDataRawExportDto waterDataRawExportDto2 = createWaterDataRawExportDto(
                    "DeviceID2", "Enterprise2", new Date(),
                    "TemperatureSensor2", "TemperatureProbe2", "Humidity2", "Intensity2",
                    Arrays.asList("4300", "4250", "5010", "5510", "1600"),
                    Arrays.asList("2", "3", "4", "5", "6"),
                    Arrays.asList("6", "5", "4", "3", "2"),
                    Arrays.asList("7", "8", "9", "1.0", "1.1"),
                    Arrays.asList("1.0", "1.1", "1.2", "1.3", "1.4")
            );

            byte[] chartImageBytes1 = saveChartAsImage(createLineChart(graphDataList1));
            ByteArrayOutputStream workbookStream1 = exportChartAndWaterDataToWorkbook(chartImageBytes1, waterDataRawExportDto1, "multi_spectrum_chart_with_data_1.xlsx");

            byte[] chartImageBytes2 = saveChartAsImage(createLineChart(graphDataList2));
            ByteArrayOutputStream workbookStream2 = exportChartAndWaterDataToWorkbook(chartImageBytes2, waterDataRawExportDto2, "multi_spectrum_chart_with_data_2.xlsx");

            ByteArrayOutputStream byteArrayOutputStream = zipWorkbooksToZipStream(workbookStream1, workbookStream2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createChartSheet(Workbook workbook, byte[] chartImageBytes) {
        Sheet chartSheet = workbook.createSheet("曲线图");
        int pictureIdx = workbook.addPicture(chartImageBytes, Workbook.PICTURE_TYPE_PNG);
        CreationHelper helper = workbook.getCreationHelper();
        Drawing<?> drawing = chartSheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(0);
        anchor.setRow1(0);
        Picture picture = drawing.createPicture(anchor, pictureIdx);
        picture.resize();
    }

    private static void createDataSheet(Workbook workbook, WaterDataRawExportDto waterDataRawExportDto) {
        Sheet dataSheet = workbook.createSheet("水质原始数据");
        createHeaderRow(dataSheet, "主机id", "绑定企业", "采集时间", "主板温度", "温度", "湿度", "能量强度", "波长", "暗电流", "参比", "样品", "吸光度");
        createDataRow(dataSheet, waterDataRawExportDto);
    }

    private static List<SpectrumGraphDataDto> createGraphDataList(List<String> wavelengthList, List<String>... absorbanceLists) {
        List<SpectrumGraphDataDto> graphDataList = new ArrayList<>();
        for (List<String> absorbanceList : absorbanceLists) {
            graphDataList.add(new SpectrumGraphDataDto(wavelengthList, absorbanceList));
        }
        return graphDataList;
    }

    private static WaterDataRawExportDto createWaterDataRawExportDto(String deviceId, String departName, Date collectTime,
                                                                     String temperatureSensor, String temperatureProbe,
                                                                     String humidity, String intensity,
                                                                     List<String> wavelengthList, List<String> darkCurrentList,
                                                                     List<String> referenceList, List<String> sampleList,
                                                                     List<String> absorbanceList) {
        WaterDataRawExportDto waterDataRawExportDto = new WaterDataRawExportDto();
        waterDataRawExportDto.setDeviceId(deviceId);
        waterDataRawExportDto.setDepartName(departName);
        waterDataRawExportDto.setCollectTime(collectTime);
        waterDataRawExportDto.setTemperatureSensor(temperatureSensor);
        waterDataRawExportDto.setTemperatureProbe(temperatureProbe);
        waterDataRawExportDto.setHumidity(humidity);
        waterDataRawExportDto.setIntensity(intensity);
        waterDataRawExportDto.setWavelengthList(wavelengthList);
        waterDataRawExportDto.setDarkCurrentList(darkCurrentList);
        waterDataRawExportDto.setReferenceList(referenceList);
        waterDataRawExportDto.setSampleList(sampleList);
        waterDataRawExportDto.setAbsorbanceList(absorbanceList);
        return waterDataRawExportDto;
    }

    private static ByteArrayOutputStream zipWorkbooksToZipStream(ByteArrayOutputStream... workbookStreams) throws IOException {
        ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(zipOutputStream)) {
            for (int i = 0; i < workbookStreams.length; i++) {
                ByteArrayOutputStream workbookStream = workbookStreams[i];
                addToZip(workbookStream.toByteArray(), zos, "multi_spectrum_chart_with_data_" + (i + 1) + ".xlsx");
            }
        }
        return zipOutputStream;
    }

    private static void addToZip(byte[] data, ZipOutputStream zos, String entryName) throws IOException {
        ZipEntry zipEntry = new ZipEntry(entryName);
        zos.putNextEntry(zipEntry);
        zos.write(data);
        zos.closeEntry();
    }

    private static JFreeChart createLineChart(List<SpectrumGraphDataDto> graphDataList) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (int i = 0; i < graphDataList.size(); i++) {
            SpectrumGraphDataDto graphData = graphDataList.get(i);
            XYSeries series = new XYSeries("Spectrum Data " + (i + 1));
            for (int j = 0; j < graphData.getWavelengthList().size(); j++) {
                double wavelength = Double.parseDouble(graphData.getWavelengthList().get(j));
                double absorbance = Double.parseDouble(graphData.getAbsorbanceList().get(j));
                series.add(wavelength, absorbance);
            }
            dataset.addSeries(series);
        }
        return ChartFactory.createXYLineChart(
                "Spectrum Data", "Wavelength", "Absorbance", dataset,
                PlotOrientation.VERTICAL, true, true, false
        );
    }


    private static byte[] saveChartAsImage(JFreeChart chart) throws IOException {
        BufferedImage image = chart.createBufferedImage(CHART_WIDTH, CHART_HEIGHT);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ChartUtils.writeBufferedImageAsPNG(byteArrayOutputStream, image);
        return byteArrayOutputStream.toByteArray();
    }

    private static ByteArrayOutputStream exportChartAndWaterDataToWorkbook(byte[] chartImageBytes, WaterDataRawExportDto waterDataRawExportDto, String excelFileName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            createChartSheet(workbook, chartImageBytes);
            createDataSheet(workbook, waterDataRawExportDto);

            ByteArrayOutputStream workbookStream = new ByteArrayOutputStream();
            workbook.write(workbookStream);
            return workbookStream;
        }
    }

    private static void createHeaderRow(Sheet sheet, String... headers) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private static void createDataRow(Sheet sheet, WaterDataRawExportDto waterDataRawExportDto) {
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(waterDataRawExportDto.getDeviceId());
        row.createCell(1).setCellValue(waterDataRawExportDto.getDepartName());
        row.createCell(2).setCellValue(waterDataRawExportDto.getCollectTime().toString());
        row.createCell(3).setCellValue(waterDataRawExportDto.getTemperatureSensor());
        row.createCell(4).setCellValue(waterDataRawExportDto.getTemperatureProbe());
        row.createCell(5).setCellValue(waterDataRawExportDto.getHumidity());
        row.createCell(6).setCellValue(waterDataRawExportDto.getIntensity());

        List<List<String>> allRowList = createAllRowList(waterDataRawExportDto);

        for (int i = 0; i < allRowList.size(); i++) {
            row = sheet.getRow(i + 1);
            if (row == null) {
                row = sheet.createRow(i + 1);
            }
            int colIndex = 7; // 从第7列开始为 List 数据
            for (String cellValue : allRowList.get(i)) {
                row.createCell(colIndex++).setCellValue(cellValue);
            }
        }

    }

    private static List<List<String>> createAllRowList(WaterDataRawExportDto waterDataRawExportDto) {
        List<String> wavelengthList = waterDataRawExportDto.getWavelengthList();
        List<String> darkCurrentList = waterDataRawExportDto.getDarkCurrentList();
        List<String> referenceList = waterDataRawExportDto.getReferenceList();
        List<String> sampleList = waterDataRawExportDto.getSampleList();
        List<String> absorbanceList = waterDataRawExportDto.getAbsorbanceList();
        int wavelengthSize = wavelengthList.size();
        int darkCurrentSize = darkCurrentList.size();
        int referenceSize = referenceList.size();
        int sampleSize = sampleList.size();
        int absorbanceSize = absorbanceList.size();
        int maxSize = NumberUtil.max(wavelengthSize, darkCurrentSize,
                referenceSize, sampleSize, absorbanceSize);

        String emptyStr = "";
        List<List<String>> allRowList = new ArrayList<>(maxSize);
        for (int i = 0; i < maxSize; i++) {
            List<String> rowList = new ArrayList<>(5);
            if (i < wavelengthSize) {
                rowList.add(wavelengthList.get(i));
            } else {
                rowList.add(emptyStr);
            }
            if (i < darkCurrentSize) {
                rowList.add(darkCurrentList.get(i));
            } else {
                rowList.add(emptyStr);
            }
            if (i < referenceSize) {
                rowList.add(referenceList.get(i));
            } else {
                rowList.add(emptyStr);
            }
            if (i < sampleSize) {
                rowList.add(sampleList.get(i));
            } else {
                rowList.add(emptyStr);
            }
            if (i < absorbanceSize) {
                rowList.add(absorbanceList.get(i));
            } else {
                rowList.add(emptyStr);
            }
            allRowList.add(rowList);
        }
        return allRowList;
    }

}

@Data
class SpectrumGraphDataDto {
    private List<String> wavelengthList;
    private List<String> absorbanceList;

    public SpectrumGraphDataDto(List<String> wavelengthList, List<String> absorbanceList) {
        this.wavelengthList = wavelengthList;
        this.absorbanceList = absorbanceList;
    }
}

@Data
class WaterDataRawExportDto {

    private String deviceId;
    private String departName;
    private Date collectTime;
    private String temperatureSensor;
    private String temperatureProbe;
    private String humidity;
    private String intensity;
    private List<String> wavelengthList;
    private List<String> darkCurrentList;
    private List<String> referenceList;
    private List<String> sampleList;
    private List<String> absorbanceList;
}
