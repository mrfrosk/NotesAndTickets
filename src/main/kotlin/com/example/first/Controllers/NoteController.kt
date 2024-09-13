package com.example.first.Controllers

import com.example.first.Services.NoteService
import com.example.first.database.dto.NoteDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/notes")
class NoteController {

    @Autowired
    lateinit var noteService: NoteService

    @PostMapping("/new")
    fun createNote(@RequestBody note: String): ResponseEntity<*> {
        val newNote = Json.decodeFromString<NoteDto>(note)
        noteService.createNote(newNote)
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }

    @GetMapping("/{user-id}")
    fun getUserNotes(@PathVariable("user-id") userId: UUID): ResponseEntity<*> {
        val users = noteService.getUserNotes(userId)
        return ResponseEntity.status(HttpStatus.OK).body(Json.encodeToString(users))
    }

    @GetMapping("/note/{title}")
    fun getByTitle(@PathVariable("title") title: String): ResponseEntity<*> {
        val note = noteService.getNote(title).toDto()
        return ResponseEntity.status(HttpStatus.OK).body(Json.encodeToString(note))
    }

    @PutMapping("/{title}")
    fun updateNote(
        @PathVariable("title") title: String,
        @RequestBody text: String
    ) {
        noteService.updateNote(title, text)
    }

    @DeleteMapping("/{title}")
    fun deleteNote(@PathVariable title: String) {
        noteService.deleteNote(title)
    }
}