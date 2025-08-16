package com.fitness.activityservice.controlller;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/api/activities") //map this entire controller to particular endpoint
@AllArgsConstructor //for spring to automatically inject "activityService" dependency
public class ActivityController {

    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request, @RequestHeader("X-User-ID") String userId) {
        if (userId != null) {
            request.setUserId(userId);
        }
        //a dto class thats going to return response and this class is going to define the response
           //ActivityRes defines res. that this particular endpt. will give
        //ActivityReq defines req that this endpt. is gonna receive
        return ResponseEntity.ok(activityService.trackActivity(request));

    }
    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@RequestHeader("X-User-ID")String userId) { //return activity of specific user
        return ResponseEntity.ok(activityService.getUserActivities(userId));

    }
    @GetMapping("/{activityId}") //need to get single activity by id
    public ResponseEntity<ActivityResponse> getActivity(@PathVariable String activityId) { //return activity of specific user
        return ResponseEntity.ok(activityService.getActivityById(activityId));

    }
}
