package com.fitness.aiservice.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document(collection = "recommendations")
@Data
@Builder
public class Recommendation {
    private String id; //recco id
    private String activityId;
    private String userId;
    private String activityType;
    private String recommendation;
    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safety;

    private List<String> createdAt;
}
