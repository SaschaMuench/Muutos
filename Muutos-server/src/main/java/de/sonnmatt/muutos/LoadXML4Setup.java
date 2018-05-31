package de.sonnmatt.muutos;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class LoadXML4Setup {

	private static final Logger log = LogManager.getLogger(LoadXML4Setup.class);
	private final String classname = this.getClass().getSimpleName();
	
	private Document xmlDoc;
	
	public LoadXML4Setup() {
	}
	
	public void loadXmlFile(String filename) {
		log.traceEntry("loadXmlFile({})", this.getClass().getSimpleName(), filename);
		log.trace("file URI: {}", this.getClass().getClassLoader().getResource(filename).toExternalForm());
		File fXmlFile = new File(this.getClass().getClassLoader().getResource(filename).getFile());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(false);
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			xmlDoc = dBuilder.parse(fXmlFile);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmlDoc.getDocumentElement().normalize();
	}
	
	public NodeList getNodes(String nodeName) {
		log.traceEntry("{} getNodes({})", classname, nodeName);
		return xmlDoc.getElementsByTagName(nodeName);
	}
	
	public HashMap<String, String> getNodeData(String xPath) {
		log.traceEntry("{}.getNodeData({})", classname, xPath);
		
		HashMap<String, String> nodeData = new HashMap<>();
		
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = null;
		Object result = null;
		NodeList nodes = null;
		try {
			expr = xpath.compile(xPath);
			result = expr.evaluate(xmlDoc, XPathConstants.NODESET);
			nodes = ((NodeList) result).item(0).getChildNodes();
		} catch (XPathExpressionException | NullPointerException e) {
			return log.traceExit(String.format("%s.getNodeData(%s): null", classname, xPath), null);
		}

		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
			    log.trace("{}.getNodeData() subNodes: {}.{}.{}", classname, nodes.item(i).getNodeName(), nodes.item(i).getNodeValue(), nodes.item(i).getTextContent());
			    nodeData.put(nodes.item(i).getNodeName(), nodes.item(i).getTextContent());
			}
		}
		return log.traceExit(String.format("%s.getNodeData(%s)", classname, xPath), nodeData);
	}
	
	public HashMap<Integer, HashMap<String, String>> getMultiNodeData(String xPath) {
		log.traceEntry("{}.getNodeData({})", classname, xPath);
		HashMap<Integer, HashMap<String, String>> nodeData = new HashMap<>();
		
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = null;
		Object result = null;
		try {
			expr = xpath.compile(xPath);
			result = expr.evaluate(xmlDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NodeList hNodes = (NodeList) result;
		log.trace("{}.getNodeData() childnode: {}", classname, hNodes.getLength());
		for (int j = 0; j < hNodes.getLength(); j++) {
			NodeList nodes = hNodes.item(j).getChildNodes();
			HashMap<String, String> subNodeData = new HashMap<>();
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				    log.trace("{}.getNodeData() subNodes: {}.{}", classname, nodes.item(i).getNodeName(), nodes.item(i).getTextContent());
				    subNodeData.put(nodes.item(i).getNodeName(), nodes.item(i).getTextContent());
				}
			}
			nodeData.put(j, subNodeData);
		}
		return log.traceExit(String.format("%s.getNodeData(%s)", classname, xPath), nodeData);
	}

	public HashMap<String, String> getNodeAttributeData(String xPath, String codeAttribute) {
		log.traceEntry("{}.getNodeAttributeData({}, {})", classname, xPath, codeAttribute);
		HashMap<String, String> nodeData = new HashMap<>();
		
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = null;
		Object result = null;
		NodeList nodes = null;
		try {
			expr = xpath.compile(xPath);
			result = expr.evaluate(xmlDoc, XPathConstants.NODESET);
			nodes = ((NodeList) result).item(0).getChildNodes();
		} catch (XPathExpressionException | NullPointerException e) {
			return log.traceExit(String.format("%s.getNodeAttributeData(%s, %s): null", classname, xPath, codeAttribute), null);
		}
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				String attribValue = nodes.item(i).getAttributes().getNamedItem(codeAttribute).getNodeValue();
				log.trace("{}.getMultiNodeAttributeData() subNodes: {}({}).{}", classname, nodes.item(i).getNodeName(), attribValue, nodes.item(i).getTextContent());
			    nodeData.put(attribValue, nodes.item(i).getTextContent());
			}
		}
		return log.traceExit(String.format("%s.getMultiNodeAttributeData(%s, %s)", classname, xPath, codeAttribute), nodeData);
	}

	public HashMap<Integer, HashMap<String, String>> getMultiNodeAttributeData(String xPath, String codeAttribute) {
		log.traceEntry("{}.getMultiNodeAttributeData({}, {})", classname, xPath, codeAttribute);
		HashMap<Integer, HashMap<String, String>> nodeData = new HashMap<>();
		
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression expr = null;
		Object result = null;
		try {
			expr = xpath.compile(xPath);
			result = expr.evaluate(xmlDoc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NodeList hNodes = (NodeList) result;
		log.trace("{}.getMultiNodeAttributeData() childnode: {}", classname, hNodes.getLength());
		for (int j = 0; j < hNodes.getLength(); j++) {
			NodeList nodes = hNodes.item(j).getChildNodes();
			HashMap<String, String> subNodeData = new HashMap<>();
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				    String attribValue = nodes.item(i).getAttributes().getNamedItem(codeAttribute).getNodeValue();
				    log.trace("{}.getMultiNodeAttributeData() subNodes: {}({}).{}", classname, nodes.item(i).getNodeName(), attribValue, nodes.item(i).getTextContent());
				    subNodeData.put(attribValue, nodes.item(i).getTextContent());
				}
			}
			nodeData.put(j, subNodeData);
		}
		return log.traceExit(String.format("%s.getMultiNodeAttributeData(%s, %s)", classname, xPath, codeAttribute), nodeData);
	}
}