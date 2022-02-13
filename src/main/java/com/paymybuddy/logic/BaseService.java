package com.paymybuddy.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paymybuddy.logic.gson.LocalDateTimeDeserializer;
import com.paymybuddy.logic.gson.LocalDateTimeSerializer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BaseService {

    /**
     * Generates a HttpStatus.CREATED response including the new database entry in JSON format
     *
     * @param object the created object
     * @return ResponseEntity containing the output
     */
    public ResponseEntity<String> createdResponse(Object object) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = builder.setPrettyPrinting().create();
        String responseString = gson.toJson(object);
        return new ResponseEntity<String>(responseString, new HttpHeaders(), HttpStatus.CREATED);
    }

}
