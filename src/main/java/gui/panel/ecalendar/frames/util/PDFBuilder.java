package gui.panel.ecalendar.frames.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import gui.panel.ecalendar.Main;
import gui.panel.ecalendar.data.Converter;
import gui.panel.ecalendar.data.ExtendCalendarRow;
import gui.panel.ecalendar.styles.GeneralStyles;
import p.calendar.data.CalendarRow.COUNTRY;

public class PDFBuilder {

	public static PDFBuilder getInstance() {
		if (instance == null) {
			instance = new PDFBuilder();
		}
		return instance;
	}

	private PDFBuilder() {
		BaseFont externalBaseFont = null;
		String fontName = GeneralStyles.ARIAL_CYRILLIC_FONT_NAME;
		InputStream in = null;
		try {
			System.out.println("Loading font <" + fontName + "> from resources ...");
			in = Main.class.getResourceAsStream("/" + fontName);
			if (in != null) {
				externalBaseFont = BaseFont.createFont(in, fontName);
				System.out.println("Font <" + fontName + "> successfully loaded");
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOHelper.closeStream(in);
		}

		if (externalBaseFont != null) {
			defaultFont = new Font(externalBaseFont, 12);
		} else {
			System.out.println("Error occurred while loading the font: " + fontName);

			defaultFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
			System.out.println("Set default font: TIMES_ROMAN. Without the support of Cyrillic");
		}
	}

	public boolean createDocument(ExtendCalendarRow extRow, BufferedImage chartImage, String path) {
		try {
			document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(path));
			document.open();
			addMetaData(extRow);
			addContent(extRow, chartImage);
			document.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void addMetaData(ExtendCalendarRow extRow) {
		document.addTitle("Economic Calendar Event");
		document.addSubject(extRow.getEvent());
		document.addAuthor(System.getProperty("user.name"));
		document.addCreator("Terminal-Info");
	}

	private void addContent(ExtendCalendarRow extRow, BufferedImage chartImage)
			throws DocumentException, IOException {

		document.add(new Paragraph("Событие: " + extRow.getEvent(), defaultFont));
		document.add(new Paragraph("Дата: " + extRow.getDateTime(), defaultFont));
		document.add(new Paragraph("Страна: " + Converter.translateCountry(COUNTRY.valueOf(extRow.getCountry())), defaultFont));
		document.add(new Paragraph("Важность: " + extRow.getImportance(), defaultFont));
		document.add(new Paragraph("Категория: " + extRow.getCategory(), defaultFont));
		document.add(new Paragraph("Фактическое значение: " + extRow.getValue(), defaultFont));
		document.add(new Paragraph("Прогноз: " + extRow.getForecast(), defaultFont));
		document.add(new Paragraph("Предыдущее значение: " + extRow.getPrevValue(), defaultFont));
		document.add(new Paragraph("Источник: " + extRow.getSource(), defaultFont));

		if (chartImage != null) {
			addEmptyLine();
			addChart(chartImage);
		}
	}

	private void addEmptyLine() throws DocumentException {
		document.add(new Paragraph(" ", defaultFont));
	}

	private void addChart(BufferedImage chartImage) {
		Image image = null;
		try {
			ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();

			ImageIO.write(chartImage, "png", byteArrayStream);

			byte[] bytes = byteArrayStream.toByteArray();

			image = Image.getInstance(bytes);
			image.scaleAbsolute(500, 245);

			document.add(image);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadElementException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	@SuppressWarnings("unused")
	private void addOtherContent() throws DocumentException {
		Anchor anchor = new Anchor("First Chapter", defaultFont);
		anchor.setName("First Chapter");

		Chapter chapter = new Chapter(new Paragraph(anchor), 1);

		Paragraph p = new Paragraph("Subcategory 1", defaultFont);
		Section section = chapter.addSection(p);
		section.add(new Paragraph("Hello"));

		p = new Paragraph("Subcategory 2", defaultFont);
		section = chapter.addSection(p);
		section.add(new Paragraph("Paragraph 1"));
		section.add(new Paragraph("Paragraph 2"));
		section.add(new Paragraph("Paragraph 3"));

		createList(section);
		Paragraph paragraph = new Paragraph();
		addEmptyLine(paragraph, 5);
		section.add(paragraph);

		createTable(section);

		document.add(chapter);

		anchor = new Anchor("Second Chapter", defaultFont);
		anchor.setName("Second Chapter");

		chapter = new Chapter(new Paragraph(anchor), 1);

		p = new Paragraph("Subcategory", defaultFont);
		section = chapter.addSection(p);
		section.add(new Paragraph("This is a very important message"));

		document.add(chapter);
	}

	private static void createTable(Section subCatPart)
			throws BadElementException {
		PdfPTable table = new PdfPTable(3);

		// t.setBorderColor(BaseColor.GRAY);
		// t.setPadding(4);
		// t.setSpacing(4);
		// t.setBorderWidth(1);

		PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Table Header 2"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Table Header 3"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		table.setHeaderRows(1);

		table.addCell("1.0");
		table.addCell("1.1");
		table.addCell("1.2");
		table.addCell("2.1");
		table.addCell("2.2");
		table.addCell("2.3");

		subCatPart.add(table);

	}

	private static void createList(Section subCatPart) {
		List list = new List(true, false, 10);
		list.add(new ListItem("First point"));
		list.add(new ListItem("Second point"));
		list.add(new ListItem("Third point"));
		subCatPart.add(list);
	}

	private static PDFBuilder instance = null;
	private final Font defaultFont;
	private Document document;
}
