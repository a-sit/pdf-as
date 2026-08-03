package at.gv.egiz.pdfas.lib.impl.pdfbox3

import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSBase

fun COSArray.asDereferencedSequence() : Sequence<COSBase?> =
    (0..<size()).asSequence().map(::getObject)
