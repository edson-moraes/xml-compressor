package br.com.unisys.xmlexitools;

import com.siemens.ct.exi.core.CodingMode;
import com.siemens.ct.exi.core.FidelityOptions;

public class XmlCompressor {

    private final CodingMode codingMode;
    private final FidelityOptions fidelityOptions;

    /**
     * @param codingMode
     * @param preserveComments               <p>CM(Comment) events will be preserved if <b><tt>true</tt></b>.</p>
     * @param preserveProcessingInstructions <p>PI events will be preserved if <b><tt>true</tt></b>. A Processing
     *                                       Instruction (PI) is an SGML and XML node type, which may occur anywhere in
     *                                       the document, intended to carry instructions to the application.</p>
     * @param preserveDTDAndEntityRef        <p>DT and ER events can be preserved <b><tt>true</tt></b>.</p>
     *                                       <p>A document type (DT) declaration information item maps to a DOCTYPE. A
     *                                       document type (DOCTYPE) declaration consists of an internal, or references
     *                                       an external Document Type Definition (DTD). It can also have a combination
     *                                       of both internal and external DTDs. The DTD defines the constraints on the
     *                                       structure of an XML document.</p>
     * @param preservePrefixes
     * @param preserveLexicalValues
     * @param enableSelfContainedElements
     * @param stricSchemaInterpretation
     * @return
     */
    public static XmlCompressor init(CodingMode codingMode,
                                     boolean preserveComments,
                                     boolean preserveProcessingInstructions,
                                     boolean preserveDTDAndEntityRef,
                                     boolean preservePrefixes,
                                     boolean preserveLexicalValues,
                                     boolean enableSelfContainedElements,
                                     boolean stricSchemaInterpretation) {

        return null;


    }

    public XmlCompressor() {
        this.codingMode = CodingMode.BIT_PACKED;
        this.fidelityOptions = FidelityOptions.createAll();
    }

    public XmlCompressor(CodingMode codingMode, FidelityOptions fidelityOptions) {
        this.codingMode = codingMode;
        this.fidelityOptions = fidelityOptions;
    }


}
