package com.face.recognittion.repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AttendanceRepositoryCustomImpl implements AttendanceRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Map<String, Object>> findAttendanceWithClassDetails(String studentId, String date) {
        List<Document> pipeline = new ArrayList<>();

        // Match stage to filter by studentId and date
        pipeline.add(new Document("$match", new Document("studentId", studentId).append("date", date)));

        // Lookup stage to join with ClassesDetail collection
        pipeline.add(new Document("$lookup", new Document("from", "classes_detail")
                .append("localField", "classDetailId")
                .append("foreignField", "_id")
                .append("as", "classDetails")));

        // Unwind stage to flatten the classDetails array
        pipeline.add(new Document("$unwind", "$classDetails"));

        // Project stage to include only necessary fields
        pipeline.add(new Document("$project", new Document("studentId", 1)
                .append("date", 1)
                .append("checkIn", 1)
                .append("classDetails", 1)));

        AggregateIterable<Document> result = mongoTemplate.getCollection("attendance").aggregate(pipeline);

        List<Map<String, Object>> output = new ArrayList<>();
        for (Document doc : result) {
            output.add(doc);
        }

        return output;
    }
}
