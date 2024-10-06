package com.example.first.Controllers

import com.example.first.Services.NoteService
import com.example.first.database.dto.NoteDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(Mapping.NOTES)
class NoteController {

    @Autowired
    lateinit var noteService: NoteService

    @GetMapping("/{id}")
    suspend fun getUserNotes(@PathVariable("id") userId: UUID): ResponseEntity<*> {
        val users = newSuspendedTransaction {
            noteService.getUserNotes(userId)
        }
        return ResponseEntity.ok().body(Json.encodeToString(users))
    }

    @GetMapping("/note/{title}")
    suspend fun getByTitle(@PathVariable("title") title: String): ResponseEntity<*> {
        val note = newSuspendedTransaction {
            noteService.getNote(title).toDto()
        }
        return ResponseEntity.status(HttpStatus.OK).body(Json.encodeToString(note))
    }

    @PostMapping("/note/new")
    suspend fun createNote(@RequestBody note: String): ResponseEntity<*> {
        val newNote = Json.decodeFromString<NoteDto>(note)
        newSuspendedTransaction {
            noteService.createNote(newNote)
        }
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }

    @PutMapping("/note/{title}")
    suspend fun updateNote(
        @PathVariable("title") title: String,
        @RequestBody text: String
    ) {
        newSuspendedTransaction {
            noteService.updateNote(title, text)
        }
    }

    @DeleteMapping("/note/{title}")
    suspend fun deleteNote(@PathVariable title: String) {
        newSuspendedTransaction {
            noteService.deleteNote(title)
        }
    }
}