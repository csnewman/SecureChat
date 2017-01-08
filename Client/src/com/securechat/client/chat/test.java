package com.securechat.client.chat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class test {

	public static void main(String[] args) throws Exception {

		// FileWriter fw = new FileWriter(new File("cat.xml"));
		// fw.write("\u003c?xml version\u003d\"1.0\"
		// encoding\u003d\"UTF-8\"?\u003e\r\n\u003cassessmentItem
		// xmlns\u003d\"http://www.imsglobal.org/xsd/imsqti_v2p1\"
		// xmlns:ns2\u003d\"http://www.w3.org/2001/XInclude\"
		// xmlns:ns3\u003d\"http://www.w3.org/1999/xlink\"
		// xmlns:ns4\u003d\"http://www.w3.org/1998/Math/MathML\"
		// xmlns:ns5\u003d\"http://www.imsglobal.org/xsd/imslip_v1p0\"
		// identifier\u003d\"Present:Present:Ebook\" title\u003d\"\"
		// adaptive\u003d\"false\" timeDependent\u003d\"false\"
		// template\u003d\"default\"\u003e\u003coutcomeDeclaration
		// identifier\u003d\"SCORE\" cardinality\u003d\"single\"
		// baseType\u003d\"integer\" /\u003e\u003citemBody\u003e\u003cdiv
		// id\u003d\"rubric\" /\u003e\u003cdiv
		// id\u003d\"content\"\u003e\u003cdiv
		// id\u003d\"contentblock\"\u003e\u003cebook cover\u003d\"true\"
		// offset\u003d\"14\" title\u003d\"AQA Physics Year 1 and AS\"
		// audiocontrols\u003d\"false\" autosave\u003d\"false\"
		// effects\u003d\"false\"\u003e\u003clinks
		// /\u003e\u003cpages\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109470_ebook_ref100562_1.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109477_ebook_ref100562_2.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109482_ebook_ref100562_3.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109490_ebook_ref100562_4.jpg\"
		// width\u003d\"1105\" height\u003d\"1502\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109497_ebook_ref100562_5.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109505_ebook_ref100562_6.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109512_ebook_ref100562_7.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109519_ebook_ref100562_8.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109527_ebook_ref100562_9.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109533_ebook_ref100562_10.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109542_ebook_ref100562_11.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109549_ebook_ref100562_12.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109556_ebook_ref100562_13.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109564_ebook_ref100562_14.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109571_ebook_ref100562_15.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109578_ebook_ref100562_16.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109584_ebook_ref100562_17.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109590_ebook_ref100562_18.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109600_ebook_ref100562_19.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109461_ebook_ref100562_20.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109469_ebook_ref100562_21.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109476_ebook_ref100562_22.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109484_ebook_ref100562_23.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109492_ebook_ref100562_24.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109498_ebook_ref100562_25.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109506_ebook_ref100562_26.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109514_ebook_ref100562_27.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109521_ebook_ref100562_28.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109529_ebook_ref100562_29.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109537_ebook_ref100562_30.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109545_ebook_ref100562_31.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109553_ebook_ref100562_32.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109562_ebook_ref100562_33.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109569_ebook_ref100562_34.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109577_ebook_ref100562_35.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109583_ebook_ref100562_36.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109589_ebook_ref100562_37.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109596_ebook_ref100562_38.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109382_ebook_ref100562_39.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109386_ebook_ref100562_40.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109390_ebook_ref100562_41.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109393_ebook_ref100562_42.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109397_ebook_ref100562_43.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109401_ebook_ref100562_44.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109405_ebook_ref100562_45.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109409_ebook_ref100562_46.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109414_ebook_ref100562_47.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109418_ebook_ref100562_48.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109422_ebook_ref100562_49.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109427_ebook_ref100562_50.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109430_ebook_ref100562_51.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109434_ebook_ref100562_52.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109439_ebook_ref100562_53.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109445_ebook_ref100562_54.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109447_ebook_ref100562_55.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109449_ebook_ref100562_56.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109456_ebook_ref100562_57.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109460_ebook_ref100562_58.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109466_ebook_ref100562_59.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109472_ebook_ref100562_60.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109481_ebook_ref100562_61.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109489_ebook_ref100562_62.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109500_ebook_ref100562_63.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109507_ebook_ref100562_64.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109515_ebook_ref100562_65.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109523_ebook_ref100562_66.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109536_ebook_ref100562_67.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109543_ebook_ref100562_68.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109551_ebook_ref100562_69.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109560_ebook_ref100562_70.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109567_ebook_ref100562_71.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109575_ebook_ref100562_72.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109581_ebook_ref100562_73.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109591_ebook_ref100562_74.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109598_ebook_ref100562_75.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109603_ebook_ref100562_76.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109608_ebook_ref100562_77.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109240_ebook_ref100562_78.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109246_ebook_ref100562_79.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109254_ebook_ref100562_80.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109264_ebook_ref100562_81.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109273_ebook_ref100562_82.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109281_ebook_ref100562_83.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109287_ebook_ref100562_84.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109291_ebook_ref100562_85.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109305_ebook_ref100562_86.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109313_ebook_ref100562_87.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109321_ebook_ref100562_88.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109329_ebook_ref100562_89.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109338_ebook_ref100562_90.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109345_ebook_ref100562_91.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109353_ebook_ref100562_92.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109363_ebook_ref100562_93.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109371_ebook_ref100562_94.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109375_ebook_ref100562_95.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109379_ebook_ref100562_96.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109084_ebook_ref100562_97.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109092_ebook_ref100562_98.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109101_ebook_ref100562_99.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109109_ebook_ref100562_100.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109117_ebook_ref100562_101.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109125_ebook_ref100562_102.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109132_ebook_ref100562_103.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109138_ebook_ref100562_104.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109146_ebook_ref100562_105.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109156_ebook_ref100562_106.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109163_ebook_ref100562_107.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109170_ebook_ref100562_108.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109178_ebook_ref100562_109.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109192_ebook_ref100562_110.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109196_ebook_ref100562_111.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109206_ebook_ref100562_112.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109215_ebook_ref100562_113.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109223_ebook_ref100562_114.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109231_ebook_ref100562_115.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109457_ebook_ref100562_116.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109463_ebook_ref100562_117.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109473_ebook_ref100562_118.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109480_ebook_ref100562_119.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109488_ebook_ref100562_120.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109496_ebook_ref100562_121.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109504_ebook_ref100562_122.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109513_ebook_ref100562_123.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109520_ebook_ref100562_124.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109528_ebook_ref100562_125.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109535_ebook_ref100562_126.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109544_ebook_ref100562_127.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109550_ebook_ref100562_128.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109559_ebook_ref100562_129.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109570_ebook_ref100562_130.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109576_ebook_ref100562_131.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109582_ebook_ref100562_132.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109587_ebook_ref100562_133.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109594_ebook_ref100562_134.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109383_ebook_ref100562_135.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109387_ebook_ref100562_136.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109391_ebook_ref100562_137.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109395_ebook_ref100562_138.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109399_ebook_ref100562_139.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109403_ebook_ref100562_140.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109407_ebook_ref100562_141.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109410_ebook_ref100562_142.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109413_ebook_ref100562_143.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109417_ebook_ref100562_144.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109421_ebook_ref100562_145.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109425_ebook_ref100562_146.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109429_ebook_ref100562_147.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109433_ebook_ref100562_148.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109436_ebook_ref100562_149.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109438_ebook_ref100562_150.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109444_ebook_ref100562_151.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109446_ebook_ref100562_152.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109450_ebook_ref100562_153.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109453_ebook_ref100562_154.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109388_ebook_ref100562_155.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109394_ebook_ref100562_156.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109398_ebook_ref100562_157.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109402_ebook_ref100562_158.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109406_ebook_ref100562_159.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109411_ebook_ref100562_160.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109415_ebook_ref100562_161.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109419_ebook_ref100562_162.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109423_ebook_ref100562_163.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109426_ebook_ref100562_164.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109431_ebook_ref100562_165.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109435_ebook_ref100562_166.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109440_ebook_ref100562_167.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109442_ebook_ref100562_168.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109448_ebook_ref100562_169.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109452_ebook_ref100562_170.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109455_ebook_ref100562_171.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109459_ebook_ref100562_172.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109462_ebook_ref100562_173.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109385_ebook_ref100562_174.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109389_ebook_ref100562_175.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109392_ebook_ref100562_176.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109396_ebook_ref100562_177.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109400_ebook_ref100562_178.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109404_ebook_ref100562_179.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109408_ebook_ref100562_180.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109412_ebook_ref100562_181.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109416_ebook_ref100562_182.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109420_ebook_ref100562_183.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109424_ebook_ref100562_184.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109428_ebook_ref100562_185.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109432_ebook_ref100562_186.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109437_ebook_ref100562_187.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109441_ebook_ref100562_188.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109443_ebook_ref100562_189.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109451_ebook_ref100562_190.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109454_ebook_ref100562_191.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109458_ebook_ref100562_192.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109079_ebook_ref100562_193.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109088_ebook_ref100562_194.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109100_ebook_ref100562_195.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109108_ebook_ref100562_196.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109116_ebook_ref100562_197.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109124_ebook_ref100562_198.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109134_ebook_ref100562_199.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109142_ebook_ref100562_200.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109147_ebook_ref100562_201.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109159_ebook_ref100562_202.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109167_ebook_ref100562_203.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109173_ebook_ref100562_204.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109182_ebook_ref100562_205.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109188_ebook_ref100562_206.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109199_ebook_ref100562_207.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109203_ebook_ref100562_208.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109213_ebook_ref100562_209.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109224_ebook_ref100562_210.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109232_ebook_ref100562_211.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109239_ebook_ref100562_212.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109243_ebook_ref100562_213.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109256_ebook_ref100562_214.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109262_ebook_ref100562_215.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109272_ebook_ref100562_216.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109280_ebook_ref100562_217.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109288_ebook_ref100562_218.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109294_ebook_ref100562_219.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109301_ebook_ref100562_220.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109309_ebook_ref100562_221.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109317_ebook_ref100562_222.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109328_ebook_ref100562_223.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109335_ebook_ref100562_224.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109344_ebook_ref100562_225.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109352_ebook_ref100562_226.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109364_ebook_ref100562_227.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109370_ebook_ref100562_228.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109374_ebook_ref100562_229.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109380_ebook_ref100562_230.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109384_ebook_ref100562_231.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109238_ebook_ref100562_232.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109242_ebook_ref100562_233.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109255_ebook_ref100562_234.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109263_ebook_ref100562_235.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109270_ebook_ref100562_236.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109277_ebook_ref100562_237.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109295_ebook_ref100562_238.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109302_ebook_ref100562_239.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109310_ebook_ref100562_240.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109318_ebook_ref100562_241.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109325_ebook_ref100562_242.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109333_ebook_ref100562_243.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109341_ebook_ref100562_244.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109348_ebook_ref100562_245.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109356_ebook_ref100562_246.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109362_ebook_ref100562_247.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109369_ebook_ref100562_248.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109377_ebook_ref100562_249.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109381_ebook_ref100562_250.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109078_ebook_ref100562_251.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109087_ebook_ref100562_252.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109098_ebook_ref100562_253.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109107_ebook_ref100562_254.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109115_ebook_ref100562_255.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109123_ebook_ref100562_256.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109131_ebook_ref100562_257.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109139_ebook_ref100562_258.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109151_ebook_ref100562_259.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109161_ebook_ref100562_260.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109168_ebook_ref100562_261.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109176_ebook_ref100562_262.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109184_ebook_ref100562_263.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109191_ebook_ref100562_264.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109200_ebook_ref100562_265.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109207_ebook_ref100562_266.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109216_ebook_ref100562_267.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109222_ebook_ref100562_268.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109230_ebook_ref100562_269.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109080_ebook_ref100562_270.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109086_ebook_ref100562_271.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109094_ebook_ref100562_272.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109102_ebook_ref100562_273.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109111_ebook_ref100562_274.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109118_ebook_ref100562_275.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109127_ebook_ref100562_276.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109133_ebook_ref100562_277.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109140_ebook_ref100562_278.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109148_ebook_ref100562_279.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109158_ebook_ref100562_280.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109166_ebook_ref100562_281.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109174_ebook_ref100562_282.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109181_ebook_ref100562_283.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109189_ebook_ref100562_284.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109197_ebook_ref100562_285.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109208_ebook_ref100562_286.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109214_ebook_ref100562_287.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109221_ebook_ref100562_288.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109229_ebook_ref100562_289.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109237_ebook_ref100562_290.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109241_ebook_ref100562_291.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109249_ebook_ref100562_292.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109257_ebook_ref100562_293.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109265_ebook_ref100562_294.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109271_ebook_ref100562_295.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109278_ebook_ref100562_296.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109286_ebook_ref100562_297.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109296_ebook_ref100562_298.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109303_ebook_ref100562_299.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109312_ebook_ref100562_300.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109320_ebook_ref100562_301.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109327_ebook_ref100562_302.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109336_ebook_ref100562_303.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109347_ebook_ref100562_304.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109355_ebook_ref100562_305.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109360_ebook_ref100562_306.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109365_ebook_ref100562_307.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003cpage
		// url\u003d\"//assets-runtime-production-oxed-oup.avallain.net/S3_109376_ebook_ref100562_308.jpg\"
		// width\u003d\"1106\" height\u003d\"1503\"
		// audio\u003d\"\"\u003e\u003chotspots
		// /\u003e\u003c/page\u003e\u003c/pages\u003e\u003ctoc\u003e\u003centry
		// title\u003d\"How to use this book\" page\u003d\"-6\"
		// pagelabel\u003d\"vi\" /\u003e\u003centry title\u003d\"Kerboodle\"
		// page\u003d\"-3\" pagelabel\u003d\"ix\" /\u003e\u003centry
		// title\u003d\"Skills for starting AS and A level Physics\"
		// page\u003d\"-2\" pagelabel\u003d\"x\" /\u003e\u003centry
		// title\u003d\"Section 1, Particles and radiation\" page\u003d\"2\"
		// pagelabel\u003d\"2\"\u003e\u003centry title\u003d\"1 Matter and
		// radiation\" page\u003d\"4\" pagelabel\u003d\"4\"\u003e\u003centry
		// title\u003d\"1.1 Inside the atom\" page\u003d\"4\" /\u003e\u003centry
		// title\u003d\"1.2 Stable and unstable nuclei\" page\u003d\"6\"
		// pagelabel\u003d\"6\" /\u003e\u003centry title\u003d\"1.3 Photons\"
		// page\u003d\"8\" pagelabel\u003d\"8\" /\u003e\u003centry
		// title\u003d\"1.4 Particles and antiparticles\" page\u003d\"10\"
		// pagelabel\u003d\"10\" /\u003e\u003centry title\u003d\"1.5 Particle
		// interactions\" page\u003d\"13\" pagelabel\u003d\"13\"
		// /\u003e\u003centry title\u003d\"Practice questions: Chapter 1\"
		// page\u003d\"16\" pagelabel\u003d\"16\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"2 Quarks and
		// leptons\" page\u003d\"18\" pagelabel\u003d\"18\"\u003e\u003centry
		// title\u003d\"2.1 The particle zoo\" page\u003d\"18\"
		// pagelabel\u003d\"18\" /\u003e\u003centry title\u003d\"2.2 Particle
		// sorting\" page\u003d\"20\" pagelabel\u003d\"20\" /\u003e\u003centry
		// title\u003d\"2.3 Leptons at work\" page\u003d\"22\"
		// pagelabel\u003d\"22\" /\u003e\u003centry title\u003d\"2.4 Quarks and
		// antiquarks\" page\u003d\"24\" pagelabel\u003d\"24\"
		// /\u003e\u003centry title\u003d\"2.5 Conservation rules\"
		// page\u003d\"26\" pagelabel\u003d\"26\" /\u003e\u003centry
		// title\u003d\"Practice questions: Chapter 2\" page\u003d\"28\"
		// pagelabel\u003d\"28\" /\u003e\u003c/entry\u003e\u003centry
		// title\u003d\"3 Quark phenomena\" page\u003d\"30\"
		// pagelabel\u003d\"30\"\u003e\u003centry title\u003d\"3.1 The
		// photoelectric effect\" page\u003d\"30\" pagelabel\u003d\"30\"
		// /\u003e\u003centry title\u003d\"3.2 More about photoelectricity\"
		// page\u003d\"32\" pagelabel\u003d\"32\" /\u003e\u003centry
		// title\u003d\"3.3 Collisions of electrons with atoms\"
		// page\u003d\"34\" pagelabel\u003d\"34\" /\u003e\u003centry
		// title\u003d\"3.4 Energy levels in atoms\" page\u003d\"36\"
		// pagelabel\u003d\"36\" /\u003e\u003centry title\u003d\"3.5 Energy
		// levels and spectra\" page\u003d\"39\" pagelabel\u003d\"39\"
		// /\u003e\u003centry title\u003d\"3.6 Wave-particle duality\"
		// page\u003d\"41\" pagelabel\u003d\"41\" /\u003e\u003centry
		// title\u003d\"Practice questions: Chapter 3\" page\u003d\"44\"
		// pagelabel\u003d\"44\" /\u003e\u003c/entry\u003e\u003centry
		// title\u003d\"Section 1 Summary\" page\u003d\"46\"
		// pagelabel\u003d\"46\" /\u003e\u003centry title\u003d\"End of Section
		// 1 questions\" page\u003d\"48\" pagelabel\u003d\"48\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"Section 2, Waves
		// and optics\" page\u003d\"50\" pagelabel\u003d\"50\"\u003e\u003centry
		// title\u003d\"4 Waves\" page\u003d\"52\"
		// pagelabel\u003d\"52\"\u003e\u003centry title\u003d\"4.1 Waves and
		// vibrations\" page\u003d\"52\" pagelabel\u003d\"52\"
		// /\u003e\u003centry title\u003d\"4.2 Measuring waves\"
		// page\u003d\"54\" pagelabel\u003d\"54\" /\u003e\u003centry
		// title\u003d\"4.3 Wave properties 1\" page\u003d\"56\"
		// pagelabel\u003d\"56\" /\u003e\u003centry title\u003d\"4.4 Wave
		// properties 2\" page\u003d\"58\" pagelabel\u003d\"58\"
		// /\u003e\u003centry title\u003d\"4.5 Stationary and progressive
		// waves\" page\u003d\"60\" pagelabel\u003d\"60\" /\u003e\u003centry
		// title\u003d\"4.6 More about stationary waves on strings\"
		// page\u003d\"62\" pagelabel\u003d\"62\" /\u003e\u003centry
		// title\u003d\"4.7 Using an oscilloscope\" page\u003d\"64\"
		// pagelabel\u003d\"64\" /\u003e\u003centry title\u003d\"Practice
		// questions: Chapter 4\" page\u003d\"66\" pagelabel\u003d\"66\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"5 Optics\"
		// page\u003d\"68\" pagelabel\u003d\"68\"\u003e\u003centry
		// title\u003d\"5.1 Refraction of light\" page\u003d\"68\"
		// pagelabel\u003d\"68\" /\u003e\u003centry title\u003d\"5.2 More about
		// refraction\" page\u003d\"70\" pagelabel\u003d\"70\"
		// /\u003e\u003centry title\u003d\"5.3 Total internal reflection\"
		// page\u003d\"73\" pagelabel\u003d\"73\" /\u003e\u003centry
		// title\u003d\"5.4 Double slit interference\" page\u003d\"76\"
		// pagelabel\u003d\"76\" /\u003e\u003centry title\u003d\"5.5 More about
		// interference\" page\u003d\"79\" pagelabel\u003d\"79\"
		// /\u003e\u003centry title\u003d\"5.6 Diffraction\" page\u003d\"82\"
		// pagelabel\u003d\"82\" /\u003e\u003centry title\u003d\"5.7 The
		// diffraction grating\" page\u003d\"85\" pagelabel\u003d\"85\"
		// /\u003e\u003centry title\u003d\"Practice questions: Chapter 5\"
		// page\u003d\"88\" pagelabel\u003d\"88\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"Section 2 Summary\"
		// page\u003d\"90\" pagelabel\u003d\"90\" /\u003e\u003centry
		// title\u003d\"End of Section 2 questions\" page\u003d\"92\"
		// pagelabel\u003d\"92\" /\u003e\u003c/entry\u003e\u003centry
		// title\u003d\"Section 3 Mechanics and materials\" page\u003d\"94\"
		// pagelabel\u003d\"94\"\u003e\u003centry title\u003d\"6 Forces in
		// equilibrium\" page\u003d\"96\" pagelabel\u003d\"96\"\u003e\u003centry
		// title\u003d\"6.1 Vectors and scalars\" page\u003d\"96\"
		// pagelabel\u003d\"96\" /\u003e\u003centry title\u003d\"6.2 Balanced
		// forces\" page\u003d\"100\" pagelabel\u003d\"100\" /\u003e\u003centry
		// title\u003d\"6.3 The principles of moments\" page\u003d\"103\"
		// pagelabel\u003d\"103\" /\u003e\u003centry title\u003d\"6.4 More on
		// moments\" page\u003d\"105\" pagelabel\u003d\"105\" /\u003e\u003centry
		// title\u003d\"6.5 Stability\" page\u003d\"107\" pagelabel\u003d\"107\"
		// /\u003e\u003centry title\u003d\"6.6 Equilibrium rules\"
		// page\u003d\"110\" pagelabel\u003d\"110\" /\u003e\u003centry
		// title\u003d\"6.7 Statics calculations\" page\u003d\"114\"
		// pagelabel\u003d\"114\" /\u003e\u003centry title\u003d\"Practice
		// questions: Chapter 6\" page\u003d\"116\" pagelabel\u003d\"116\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"7 On the move\"
		// page\u003d\"118\" pagelabel\u003d\"118\"\u003e\u003centry
		// title\u003d\"7.1 Speed and velocity\" page\u003d\"118\"
		// pagelabel\u003d\"118\" /\u003e\u003centry title\u003d\"7.2
		// Acceleration\" page\u003d\"120\" pagelabel\u003d\"120\"
		// /\u003e\u003centry title\u003d\"7.3 Motion along a straight line at
		// constant acceleration\" page\u003d\"122\" pagelabel\u003d\"122\"
		// /\u003e\u003centry title\u003d\"7.4 Free fall\" page\u003d\"125\"
		// pagelabel\u003d\"125\" /\u003e\u003centry title\u003d\"7.5 Motion
		// graphs\" page\u003d\"128\" pagelabel\u003d\"128\" /\u003e\u003centry
		// title\u003d\"7.6 More calculations on the motion along a straight
		// line\" page\u003d\"130\" pagelabel\u003d\"130\" /\u003e\u003centry
		// title\u003d\"7.7 Projectile motion 1\" page\u003d\"132\"
		// pagelabel\u003d\"132\" /\u003e\u003centry title\u003d\"7.8 Projectile
		// motion 2\" page\u003d\"134\" pagelabel\u003d\"134\"
		// /\u003e\u003centry title\u003d\"Practice questions: Chapter 7\"
		// page\u003d\"136\" pagelabel\u003d\"136\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"8 Newton\u0027s
		// laws of motion\" page\u003d\"138\"
		// pagelabel\u003d\"138\"\u003e\u003centry title\u003d\"8.1 Force and
		// acceleration\" page\u003d\"138\" pagelabel\u003d\"138\"
		// /\u003e\u003centry title\u003d\"8.2 Using F \u003d ma\"
		// page\u003d\"141\" pagelabel\u003d\"141\" /\u003e\u003centry
		// title\u003d\"8.3 Terminal speed\" page\u003d\"144\"
		// pagelabel\u003d\"144\" /\u003e\u003centry title\u003d\"8.4 On the
		// road\" page\u003d\"146\" pagelabel\u003d\"146\" /\u003e\u003centry
		// title\u003d\"8.5 Vehicle safety\" page\u003d\"149\"
		// pagelabel\u003d\"149\" /\u003e\u003centry title\u003d\"Practice
		// questions: Chapter 8\" page\u003d\"152\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"9 Force and
		// momentum\" page\u003d\"154\" pagelabel\u003d\"154\"\u003e\u003centry
		// title\u003d\"9.1 Momentum and impulse\" page\u003d\"154\"
		// pagelabel\u003d\"154\" /\u003e\u003centry title\u003d\"9.2 Impact
		// forces\" page\u003d\"158\" pagelabel\u003d\"158\" /\u003e\u003centry
		// title\u003d\"9.3 Conservation of momentum\" page\u003d\"161\"
		// pagelabel\u003d\"161\" /\u003e\u003centry title\u003d\"9.4 Elastic
		// and inelastic collisions\" page\u003d\"164\" pagelabel\u003d\"164\"
		// /\u003e\u003centry title\u003d\"9.5 Explosions\" page\u003d\"166\"
		// pagelabel\u003d\"166\" /\u003e\u003centry title\u003d\"Practice
		// questions: Chapter 9\" page\u003d\"168\" pagelabel\u003d\"168\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"10 Work, energy,
		// and power\" page\u003d\"170\" pagelabel\u003d\"170\"\u003e\u003centry
		// title\u003d\"10.1 Work and energy\" page\u003d\"170\"
		// pagelabel\u003d\"170\" /\u003e\u003centry title\u003d\"10.2 Kinetic
		// energy and potential energy\" page\u003d\"173\"
		// pagelabel\u003d\"173\" /\u003e\u003centry title\u003d\"10.3 Power\"
		// page\u003d\"175\" pagelabel\u003d\"175\" /\u003e\u003centry
		// title\u003d\"10.4 Energy and efficiency\" page\u003d\"177\"
		// pagelabel\u003d\"177\" /\u003e\u003centry title\u003d\"Practice
		// questions: Chapter 10\" page\u003d\"182\" pagelabel\u003d\"182\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"11 Materials\"
		// page\u003d\"184\" pagelabel\u003d\"184\"\u003e\u003centry
		// title\u003d\"11.1 Density\" page\u003d\"184\" pagelabel\u003d\"184\"
		// /\u003e\u003centry title\u003d\"11.2 springs\" page\u003d\"186\"
		// pagelabel\u003d\"186\" /\u003e\u003centry title\u003d\"11.3
		// Deformation of solids\" page\u003d\"189\" pagelabel\u003d\"189\"
		// /\u003e\u003centry title\u003d\"11.4 More about stress and strain\"
		// page\u003d\"192\" pagelabel\u003d\"192\" /\u003e\u003centry
		// title\u003d\"Practice questions: Chapter 11\" page\u003d\"194\"
		// pagelabel\u003d\"194\" /\u003e\u003c/entry\u003e\u003centry
		// title\u003d\"Section 3 Summary\" page\u003d\"196\"
		// pagelabel\u003d\"196\" /\u003e\u003centry title\u003d\"End of Section
		// 3 questions\" page\u003d\"198\" pagelabel\u003d\"198\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"Section 4
		// Electricity\" page\u003d\"200\"
		// pagelabel\u003d\"200\"\u003e\u003centry title\u003d\"12 Electric
		// current\" page\u003d\"202\" pagelabel\u003d\"202\"\u003e\u003centry
		// title\u003d\"12.1 Current and charge\" page\u003d\"202\"
		// pagelabel\u003d\"202\" /\u003e\u003centry title\u003d\"12.2 Potential
		// difference and power\" page\u003d\"204\" pagelabel\u003d\"204\"
		// /\u003e\u003centry title\u003d\"12.3 Resistance\" page\u003d\"206\"
		// pagelabel\u003d\"206\" /\u003e\u003centry title\u003d\"12.4
		// Components and their characteristics\" page\u003d\"209\"
		// pagelabel\u003d\"209\" /\u003e\u003centry title\u003d\"Practice
		// questions: Chapter 12\" page\u003d\"212\" pagelabel\u003d\"212\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"13 Direct current
		// circuits\" page\u003d\"214\" pagelabel\u003d\"214\"\u003e\u003centry
		// title\u003d\"13.1 Circuit rules\" page\u003d\"214\"
		// pagelabel\u003d\"214\" /\u003e\u003centry title\u003d\"13.2 More
		// about resistance\" page\u003d\"217\" pagelabel\u003d\"217\"
		// /\u003e\u003centry title\u003d\"13.3 Electromotive force and internal
		// resistance\" page\u003d\"220\" pagelabel\u003d\"220\"
		// /\u003e\u003centry title\u003d\"13.4 More circuit calculations\"
		// page\u003d\"223\" pagelabel\u003d\"223\" /\u003e\u003centry
		// title\u003d\"13.5 The potential divider\" page\u003d\"226\"
		// pagelabel\u003d\"226\" /\u003e\u003centry title\u003d\"Practice
		// questions: Chapter 13\" page\u003d\"228\" pagelabel\u003d\"228\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"Section 4 summary\"
		// page\u003d\"230\" pagelabel\u003d\"230\" /\u003e\u003centry
		// title\u003d\"End of Section 4 questions\" page\u003d\"232\"
		// pagelabel\u003d\"232\" /\u003e\u003c/entry\u003e\u003centry
		// title\u003d\"Further practice questions: multiple choice\"
		// page\u003d\"234\" pagelabel\u003d\"234\" /\u003e\u003centry
		// title\u003d\"Section 5 Skills in AS Physics\" page\u003d\"242\"
		// pagelabel\u003d\"242\"\u003e\u003centry title\u003d\"14 Practical
		// work in physics\" page\u003d\"243\"
		// pagelabel\u003d\"243\"\u003e\u003centry title\u003d\"14.1 Moving on
		// from GCSE\" page\u003d\"243\" pagelabel\u003d\"243\"
		// /\u003e\u003centry title\u003d\"14.2 Making careful measurements\"
		// page\u003d\"245\" pagelabel\u003d\"245\" /\u003e\u003centry
		// title\u003d\"14.3 Everyday physics instruments\" page\u003d\"247\"
		// pagelabel\u003d\"247\" /\u003e\u003centry title\u003d\"14.4 Analysis
		// and evaluation\" page\u003d\"249\" pagelabel\u003d\"249\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"15 About practical
		// assessment\" page\u003d\"252\"
		// pagelabel\u003d\"252\"\u003e\u003centry title\u003d\"15.1 Assessment
		// outline\" page\u003d\"252\" pagelabel\u003d\"252\" /\u003e\u003centry
		// title\u003d\"15.2 Direct assessment\" page\u003d\"254\"
		// pagelabel\u003d\"254\" /\u003e\u003centry title\u003d\"15.3 Indirect
		// assessment\" page\u003d\"256\" pagelabel\u003d\"256\"
		// /\u003e\u003c/entry\u003e\u003centry title\u003d\"16 More on
		// mathematical skills\" page\u003d\"258\"
		// pagelabel\u003d\"258\"\u003e\u003centry title\u003d\"16.1 Data
		// handling\" page\u003d\"258\" pagelabel\u003d\"258\"
		// /\u003e\u003centry title\u003d\"16.2 Trigonometry\" page\u003d\"260\"
		// pagelabel\u003d\"260\" /\u003e\u003centry title\u003d\"16.3 More
		// about algebra\" page\u003d\"262\" pagelabel\u003d\"262\"
		// /\u003e\u003centry title\u003d\"16.4 Straight line graphs\"
		// page\u003d\"265\" pagelabel\u003d\"265\" /\u003e\u003centry
		// title\u003d\"16.5 More on graphs\" page\u003d\"267\"
		// pagelabel\u003d\"267\" /\u003e\u003centry title\u003d\"16.6 Graphs,
		// gradients, and areas\" page\u003d\"269\" pagelabel\u003d\"269\"
		// /\u003e\u003c/entry\u003e\u003c/entry\u003e\u003centry
		// title\u003d\"For reference\" page\u003d\"272\" pagelabel\u003d\"272\"
		// /\u003e\u003centry title\u003d\"Answers to summary questions\"
		// page\u003d\"274\" pagelabel\u003d\"274\" /\u003e\u003centry
		// title\u003d\"Glossary\" page\u003d\"282\" pagelabel\u003d\"282\"
		// /\u003e\u003centry title\u003d\"Index\" page\u003d\"287\"
		// pagelabel\u003d\"287\" /\u003e\u003centry title\u003d\"Copyright and
		// Acknowledgements\" page\u003d\"292\" pagelabel\u003d\"292\"
		// /\u003e\u003c/toc\u003e\u003c/ebook\u003e\u003c/div\u003e\u003c/div\u003e\u003cdiv
		// id\u003d\"options\" /\u003e\u003cdiv id\u003d\"media\"
		// /\u003e\u003c/itemBody\u003e\u003c/assessmentItem\u003e\r\n");
		// fw.close();

		if (true) {

			File folder = new File("AQA Physics Year 1 and AS");

			for (File f : folder.listFiles()) {
				f.renameTo(new File(f.getAbsolutePath() + ".jpg"));
			}

			return;
		}

		File fXmlFile = new File("cat.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		Node ebookNode = doc.getElementsByTagName("ebook").item(0);
		String title = ebookNode.getAttributes().getNamedItem("title").getNodeValue();

		Node pagesNode = doc.getElementsByTagName("pages").item(0);
		NodeList children = pagesNode.getChildNodes();

		// <ebook cover="true" offset="14" title="AQA Physics Year 1 and AS"
		// audiocontrols="false" autosave="false" effects="false">

		new File(title).mkdirs();

		int pageCount = 0;

		for (int count = 0; count < children.getLength(); count++) {

			Node tempNode = children.item(count);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				int page = ++pageCount;
				// System.out.println(
				// "page: " + page + "(" + (page - 13) + ") , " +
				// tempNode.getAttributes().getNamedItem("url"));
				String url = tempNode.getAttributes().getNamedItem("url").getNodeValue();
				System.out.println("Saving \"" + title + "/" + title + " - Page " + page + "\"");
				saveImage("https:" + url, title + "/" + title + " - Page " + page);
				Thread.sleep(100);
			}
		}

		System.out.println("Done");
	}

	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}

}
