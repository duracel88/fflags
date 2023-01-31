package com.luk.fflags.api;

import com.luk.fflags.domain.fflags.FeatureFlagFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/fflags")
@RequiredArgsConstructor
public class FeatureFlagsController {
    private final FeatureFlagFacade facade;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(CREATED)
    public FeatureFlagDto createFeatureFlag(@Valid @RequestBody FeatureFlagDto body) {
        facade.createFeatureFlag(body.getName(), body.getDescription());
        return FeatureFlagDto.builder()
                .name(body.getName())
                .description(body.getDescription())
                .globallyEnabled(false)
                .build();
    }

    @PostMapping("/{flagName}/assignee")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(CREATED)
    public void assignUserToFeatureFlag(@PathVariable String flagName,
                                        @Valid @RequestBody FeatureFlagAssigneeDto body) {
        facade.assignFeatureFlagToUser(body.getAssigneeUsername(), flagName);
    }

    @DeleteMapping("/{flagName}/assignee/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeAssignUserFromFeatureFlag(@PathVariable String flagName,
                                                             @PathVariable String username) {
        boolean removed = facade.removeAssigneeFromFeatureFlag(username, flagName);
        if (removed) {
            return ResponseEntity.status(NO_CONTENT).build();
        } else {
            return ResponseEntity.status(NOT_FOUND).build();
        }
    }

    @GetMapping(params = {"my=true", "global=true"})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<FeatureFlagDto> getAllMineFlags(@AuthenticationPrincipal UserDetails user) {
        return facade.getAllFlagsForUser(user.getUsername())
                .stream()
                .map(flag -> FeatureFlagDto.builder()
                        .name(flag.getName())
                        .description(flag.getDescription())
                        .globallyEnabled(flag.isGloballyEnabled())
                        .build())
                .collect(toList());
    }

}
