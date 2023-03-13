package org.opentrafficsim.editor;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.djutils.exceptions.Throw;
import org.w3c.dom.Node;

/**
 * Interface for validation for xsd:key, xsd:keyRef and xsd:unique, and utility class to validate values using XSD nodes and an
 * XSD schema.
 * @author wjschakel
 */
public interface ValueValidator
{

    /** Set to store tags for which an error has been printed, to prevent repeated printing on repeated validation. */
    Set<String> SUPPRESS_ERRORS = new LinkedHashSet<>();

    /**
     * Returns message why a value is invalid, or {@code null} if the value is valid.
     * @param node XsdTreeNode; supplied to verify with context, e.g. value combinations.
     * @return String; message why a value is invalid, or {@code null} if the value is valid.
     */
    String validate(XsdTreeNode node);

    /**
     * Returns the options that a validator leaves, typically an xsd:keyref returning defined values under the reference.
     * @param node XsdTreeNode; node that is in the appropriate context.
     * @param field String; field, attribute or child element, for which to obtain the options.
     * @return List&lt;String&gt;; options, {@code null} if this is not an xsd:keyref restriction.
     */
    List<String> getOptions(XsdTreeNode node, String field);

    /**
     * Report first encountered problem in validating the value of the node.
     * @param xsdNode Node; node.
     * @param value String; value.
     * @param schema XsdSchema; schema for type retrieval.
     * @return String; first encountered problem in validating the value of the node, {@code null} if there is no problem.
     */
    static String reportInvalidValue(final Node xsdNode, final String value, final Schema schema)
    {
        Throw.when(xsdNode.equals(XiIncludeNode.XI_INCLUDE), IllegalArgumentException.class,
                "To check the value of an include, use reportInvalidInclude().");
        if (xsdNode.getChildNodes().getLength() == DocumentReader.getChildren(xsdNode, "#text").size()
                && DocumentReader.getAttribute(xsdNode, "type") == null)
        {
            // no children and no type, this is a plain tag, e.g. <STRAIGHT />, it needs no input.
            return null;
        }
        if (value == null || value.isBlank())
        {
            return "Value is empty.";
        }
        return reportTypeNonCompliance(xsdNode, "type", value, schema, null, null);
    }

    /**
     * Validates an includes file by checking whether it can be found.
     * @param value String; file name and path, possibly relative.
     * @param directory String; base directory for relative paths.
     * @return String; first encountered problem in validating the value of the include, {@code null} if there is no problem.
     */
    static String reportInvalidInclude(final String value, final String directory)
    {
        if (value == null || value.isBlank())
        {
            return "Value is empty.";
        }
        File file = new File(value);
        if (!file.isAbsolute())
        {
            if (directory == null)
            {
                return "Relative path defined but directory unknown. Try saving your work.";
            }
            file = new File(directory + value);
        }
        if (!file.exists())
        {
            return "The file cannot be found.";
        }
        return null;
    }

    /**
     * Report first encountered problem in validating the attribute value.
     * @param xsdNode Node; node, should be an xsd:attribute.
     * @param value String; value.
     * @param schema XsdSchema; schema for type retrieval.
     * @return String; first encountered problem in validating the attribute value, {@code null} if there is no problem.
     */
    static String reportInvalidAttributeValue(final Node xsdNode, final String value, final Schema schema)
    {
        String use = DocumentReader.getAttribute(xsdNode, "use");
        if ("required".equals(use) && (value == null || value.isBlank()))
        {
            return "Required value is empty.";
        }
        if (value == null || value.isBlank())
        {
            return null;
        }
        return reportTypeNonCompliance(xsdNode, "type", value, schema, null, null);
    }

    /**
     * Returns all restrictions for the given node.
     * @param xsdNode Node; node.
     * @param schema XsdSchema; schema.
     * @return List&lt;Node&gt;; list of xsd:restriction nodes applicable to the input node.
     */
    static List<Node> getRestrictions(final Node xsdNode, final Schema schema)
    {
        List<Node> restrictions = new ArrayList<>();
        reportTypeNonCompliance(xsdNode, "type", null, schema, restrictions, null);
        return restrictions;
    }

    /**
     * Returns the base type of the given node, e.g. xsd:double.
     * @param xsdNode Node; node.
     * @param schema XsdSchema; schema.
     * @return String; base type of the given node, e.g. xsd:double.
     */
    static String getBaseType(final Node xsdNode, final Schema schema)
    {
        List<String> baseType = new ArrayList<>();
        reportTypeNonCompliance(xsdNode, "type", null, schema, null, baseType);
        return baseType.get(0);
    }

    /**
     * Report first encountered problem in validating the value by a type, or when {@code value = null} scan all restrictions
     * and place them in the input list, and/or find the base type and store it in the base type list.
     * @param node Node; type node.
     * @param attribute String; "type" on normal calls, "base" on recursive calls.
     * @param value String; value.
     * @param schema XsdSchema; schema for type retrieval.
     * @param restrictions List&lt;Node&gt;; list that xsd:restriction nodes will be placed in to.
     * @param baseType List&lt;String&gt;; may be filled with 1 base type, e.g. xsd:double.
     * @return String; first encountered problem in validating the value by a type, {@code null} if there is no problem.
     */
    private static String reportTypeNonCompliance(final Node node, final String attribute, final String value,
            final Schema schema, final List<Node> restrictions, final List<String> baseType)
    {
        String type = DocumentReader.getAttribute(node, attribute); // can request "base" on recursion
        boolean isNativeType = type != null && type.startsWith("xsd:");
        if (isNativeType)
        {
            if (value == null)
            {
                if (baseType != null)
                {
                    baseType.add(type);
                    return null;
                }
                if (!node.getNodeName().equals("xsd:restriction"))
                {
                    return null;
                }
            }
            else
            {
                String report = reportNativeTypeNonCompliance(type, value);
                if (report != null || !node.getNodeName().equals("xsd:restriction"))
                {
                    return report;
                }
            }
        }
        if (type != null && !isNativeType)
        {
            String report = reportTypeNonCompliance(schema.getType(type), "base", value, schema, restrictions, baseType);
            if (value != null && report != null)
            {
                return report;
            }
            if (!node.getNodeName().equals("xsd:restriction"))
            {
                return null;
            }
        }

        switch (node.getNodeName())
        {
            case "xsd:complexType":
                Node simpleContent = DocumentReader.getChild(node, "xsd:simpleContent");
                Node extension = DocumentReader.getChild(simpleContent, "xsd:extension");
                if (extension != null)
                {
                    return reportTypeNonCompliance(extension, "base", value, schema, restrictions, baseType);
                }
                return reportTypeNonCompliance(DocumentReader.getChild(simpleContent, "xsd:restriction"), "base", value, schema,
                        restrictions, baseType);
            case "xsd:simpleType":
                return reportTypeNonCompliance(DocumentReader.getChild(node, "xsd:restriction"), "base", value, schema,
                        restrictions, baseType);
            case "xsd:element":
                if (node.getChildNodes().getLength() == 0)
                {
                    return null;
                }
                Node complexType = DocumentReader.getChild(node, "xsd:complexType");
                if (complexType != null)
                {
                    return reportTypeNonCompliance(complexType, "type", value, schema, restrictions, baseType);
                }
                Node simpleType = DocumentReader.getChild(node, "xsd:simpleType");
                return reportTypeNonCompliance(simpleType, "type", value, schema, restrictions, baseType);
            case "xsd:attribute":
                return reportTypeNonCompliance(DocumentReader.getChild(node, "xsd:simpleType"), "type", value, schema,
                        restrictions, baseType);
            case "xsd:restriction":
                if (value == null && restrictions != null)
                {
                    restrictions.add(node);
                    return null;
                }
                return reportRestrictionNonCompliance(node, value);
            case "xi:include":
                return null;
            default:
                throw new RuntimeException("Unable to validate " + node.getNodeName() + ".");
        }
    }

    /**
     * Report first encountered problem in validating a native type, e.g. xsd:int or xsd:anyURI.
     * @param type String; type.
     * @param value String; value.
     * @return String; first encountered problem in validating a native type, {@code null} if there is no problem.
     */
    private static String reportNativeTypeNonCompliance(final String type, final String value)
    {
        String valueType = "number";
        try
        {
            switch (type)
            {
                case "xsd:string":
                    return null;
                case "xsd:boolean": // "true" or "false"
                    if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
                    {
                        return "Boolean value must be \"true\" or \"false\".";
                    }
                    return null;
                case "xsd:double": // 64-bit
                    Double.valueOf(value); // might throw NumberFormatException
                    return null;
                case "xsd:float": // 32-bit
                    Float.valueOf(value); // might throw NumberFormatException
                    return null;
                case "xsd:decimal":
                    Double.valueOf(value); // might throw NumberFormatException
                    return null;
                case "xsd:int": // 32-bit signed
                    valueType = "integer";
                    Integer.valueOf(value); // might throw NumberFormatException
                    return null;
                case "xsd:long": // 64-bit signed
                    valueType = "integer";
                    Long.valueOf(value); // might throw NumberFormatException
                    return null;
                case "xsd:unsignedInt": // 32-bits, i.e. max is 2^32 - 1 = 4294967295
                    valueType = "integer";
                    long val = Long.valueOf(value); // might throw NumberFormatException
                    if (val < 0)
                    {
                        return "Integer value must be a positive integer.";
                    }
                    if (val > 4294967295L)
                    {
                        return "Integer value must be at most 4294967295.";
                    }
                    return null;
                case "xsd:positiveInteger": // arbitrary length
                    valueType = "integer";
                    if (Long.valueOf(value) < 0) // might throw NumberFormatException
                    {
                        return "Integer value must be a positive integer.";
                    }
                    return null;
                case "xsd:integer": // arbitrary length
                    valueType = "integer";
                    Long.valueOf(value); // might throw NumberFormatException
                    return null;
                case "xsd:anyURI": // RFC2396 compliant, just as URI in java
                    try
                    {
                        new URI(value);
                    }
                    catch (URISyntaxException exception)
                    {
                        return "Invalid URI.";
                    }
                    return null;
                default:
                    if (!type.startsWith("ots:"))
                    {
                        String message = "Type " + type + " cannot be validated.";
                        if (!SUPPRESS_ERRORS.contains(message))
                        {
                            System.err.println(message);
                            SUPPRESS_ERRORS.add(message);
                        }
                    }
                    return null;
            }
        }
        catch (NumberFormatException exception)
        {
            if (type.length() > 5)
            {
                String t = type.replace("xsd:", "");
                return t.substring(0, 1).toUpperCase() + t.substring(1) + " value must be a valid " + valueType + ".";
            }
            return type + " value must be a valid " + valueType + ".";
        }
    }

    /**
     * Report first encountered problem in validating the value by a restriction.
     * @param node Node; node, must be an xsd:restriction.
     * @param value String; value.
     * @return String; first encountered problem in validating the value by a restriction.
     */
    private static String reportRestrictionNonCompliance(final Node node, final String value)
    {
        Node pattern = DocumentReader.getChild(node, "xsd:pattern");
        if (pattern != null)
        {
            String patternString = DocumentReader.getAttribute(pattern, "value");
            try
            {
                if (!Pattern.matches(patternString, value))
                {
                    return "Value does not match pattern " + patternString;
                }
            }
            catch (PatternSyntaxException exception)
            {
                if (!SUPPRESS_ERRORS.contains(patternString))
                {
                    System.err.println("Could not validate value by pattern due to a PatternSyntaxException."
                            + " This means the pattern is not valid.");
                    System.err.println(exception.getMessage());
                    SUPPRESS_ERRORS.add(patternString);
                }
            }
        }
        List<Node> enumerations = DocumentReader.getChildren(node, "xsd:enumeration");
        List<String> options = new ArrayList<>();
        for (Node enumeration : enumerations)
        {
            options.add(DocumentReader.getAttribute(enumeration, "value"));
        }
        if (!options.isEmpty() && !options.contains(value))
        {
            String arrayString = options.toString();
            return "Must be any of " + arrayString.substring(1, arrayString.length() - 1) + ".";
        }
        Node minInclusive = DocumentReader.getChild(node, "xsd:minInclusive");
        if (minInclusive != null)
        {
            String val = DocumentReader.getAttribute(minInclusive, "value");
            if (Double.valueOf(value) < Double.valueOf(val))
            {
                return "Value must be above or equal to " + val + ".";
            }
        }
        Node minExclusive = DocumentReader.getChild(node, "xsd:minExclusive");
        if (minExclusive != null)
        {
            String val = DocumentReader.getAttribute(minExclusive, "value");
            if (Double.valueOf(value) <= Double.valueOf(val))
            {
                return "Value must be above " + val + ".";
            }
        }
        Node maxInclusive = DocumentReader.getChild(node, "xsd:maxInclusive");
        if (maxInclusive != null)
        {
            String val = DocumentReader.getAttribute(maxInclusive, "value");
            if (Double.valueOf(value) > Double.valueOf(val))
            {
                return "Value must be below or equal to " + val + ".";
            }
        }
        Node maxExclusive = DocumentReader.getChild(node, "xsd:maxExclusive");
        if (maxExclusive != null)
        {
            String val = DocumentReader.getAttribute(maxExclusive, "value");
            if (Double.valueOf(value) >= Double.valueOf(val))
            {
                return "Value must be below " + val + ".";
            }
        }
        return null;
    }

}