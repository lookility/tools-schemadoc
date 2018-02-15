package com.lookility.schemadoc.viewer;

import com.lookility.schemadoc.model.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelViewer implements TreeHandler {

    private PathFormatter pathFormatter;

    private Workbook wb;
    private Sheet currentSheet;
    private int rowCount;
    private CreationHelper creationHelper;

    private String language = null;
    private final TreeWalker walker;

    public ExcelViewer() {
        this.walker = new TreeWalker(this);
    }

    public synchronized void setPathFormatter(PathFormatter formatter) {
        this.pathFormatter = formatter;
    }

    public synchronized void write(File file, TreeContainer tc, String language) throws IOException {
        if (tc == null) throw new IllegalArgumentException("tree container must not be null");
        if (file == null) throw new IllegalArgumentException("file must not be null");

        this.language = language;

        // prepare file
        this.wb = new HSSFWorkbook();
        this.creationHelper = this.wb.getCreationHelper();

        for(Tree tree : tc.trees()) {
            this.currentSheet = this.wb.createSheet(tree.getName());
            this.rowCount = 0;

            // insert model
            this.walker.walk(tree);
        }

        // write generated Excel sheet to file
        FileOutputStream out = new FileOutputStream(file);
        this.wb.write(out);
        out.close();
        this.wb = null;
    }


    @Override
    public void onTreeBegin(Tree tree) {
        this.pathFormatter  = new PathFormatter(PathFormatter.NamespaceRepresentation.uriOnly, tree);
        Row header = this.currentSheet.createRow(this.rowCount);
        Cell cell;
        Font font = this.wb.createFont();
        font.setBold(true);
        CellStyle cs = this.wb.createCellStyle();
        cs.setVerticalAlignment(VerticalAlignment.TOP);
        cs.setLocked(true);

        cs.setFont(font);

        cell = header.createCell(0);
        cell.setCellValue("Path");
        cell.setCellStyle(cs);

        cell = header.createCell(1);
        cell.setCellValue("Occurrence");
        cell.setCellStyle(cs);

        cell = header.createCell(2);
        cell.setCellValue("Since");
        cell.setCellStyle(cs);


        cell = header.createCell(3);
        cell.setCellValue("Type");
        cell.setCellStyle(cs);

        cell = header.createCell(4);
        cell.setCellValue("Documentation");
        cell.setCellStyle(cs);

        this.rowCount++;
    }

    @Override
    public void onNodeBegin(ContentNode node, boolean first, boolean last) {
        addRow(node);
    }

    @Override
    public void onNodeEnd(ContentNode node, boolean first, boolean last) {
    }

    @Override
    public void onAttribute(AttributeNode attrib, boolean first, boolean last) {
        addRow(attrib);
    }

    @Override
    public void onTreeEnd() {
        this.pathFormatter = null;
    }

    private void addRow(Node node) {
        Row row = this.currentSheet.createRow(rowCount);

        CellStyle cs = this.wb.createCellStyle();
        cs.setVerticalAlignment(VerticalAlignment.TOP);
        cs.setLocked(true);

        Cell cell;

        cell = row.createCell(0);
        cell.setCellValue(pathFormatter.formatPath(node));
        cell.setCellStyle(cs);

        cell = row.createCell(1);
        cell.setCellValue(node.getOccurrence().toString());
        cell.setCellStyle(cs);

        cell = row.createCell(2);
        if (node.getAnnotation().getVersion().isPresent()) {
            cell.setCellValue(node.getAnnotation().getVersion().get().toString());
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(cs);

        cell = row.createCell(3);
        cell.setCellValue(node.getType().orElse(""));
        cell.setCellStyle(cs);

        cell = row.createCell(4);
        cell.setCellValue(node.getDocumentation().getText(this.language));
        cell.setCellStyle(cs);

        this.rowCount++;
    }
}
