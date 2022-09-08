package com.xiazhao.redbook.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.xiazhao.redbook.payload.PostDto
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
import java.nio.file.Paths


@RestController
@RequestMapping("/api/status")
class RedbookController {

    @Autowired lateinit var objectMapper: ObjectMapper

    companion object {
        const val OK_STATUS = "Server is up"
    }

    @GetMapping
    fun status() = ResponseEntity.ok(OK_STATUS)

    @GetMapping("/pdf", produces = [MediaType.APPLICATION_PDF_VALUE])
    fun getPDF(): ResponseEntity<ByteArray> {
        val pdfContent = PostDto(id = 1, title = "title", content = "content", description = "desc")
        val document = PDDocument()

        // Add text to page
        val pageWithText = PDPage()
        val textContent = PDPageContentStream(document, pageWithText)
        textContent.beginText()
        textContent.setFont(PDType1Font.TIMES_ROMAN, 12f)
        textContent.newLineAtOffset(64f, 200f)
        textContent.showText(objectMapper.writeValueAsString(pdfContent))
        textContent.endText()
        textContent.close()
        document.addPage(pageWithText)

        // Add image to page
        val pageWithImage = PDPage()
        val pdImage = PDImageXObject.createFromFile(Paths.get("src/main/resources/image/dog.jpeg").toAbsolutePath().toString(), document)
        val imageContents = PDPageContentStream(document, pageWithImage)
        imageContents.drawImage(pdImage, 70f, 250f, 200f, 150f)
        imageContents.close()
        document.addPage(pageWithImage)

        val byteArrayOutputStream = ByteArrayOutputStream()
        document.save(byteArrayOutputStream)
        document.close()

        return ResponseEntity.ok(byteArrayOutputStream.toByteArray())
    }

}
