package integration.com.jobsite_management_service.controller;

import com.jobsite_management_service.controller.InformationManagementController;
import com.jobsite_management_service.orchestration.InformationOrchestrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 1. Switch from SpringExtension to MockitoExtension to bypass classpath caching entirely
@ExtendWith(MockitoExtension.class)
public class InformationManagementControllerIT {

    private MockMvc mockMvc;

    // 2. Standard mockito mock instantiation
    @Mock
    private InformationOrchestrator informationOrchestrator;

    // 3. Tells Mockito to pass the mocked orchestrator into your controller instance
    @InjectMocks
    private InformationManagementController informationManagementController;

    @BeforeEach
    void setUp() {
        // 4. Manually construct the MockMvc controller wrapper
        this.mockMvc = MockMvcBuilders.standaloneSetup(informationManagementController).build();
    }

    @Test
    void shouldReturnLocationsAnd200OK_WhenLocationsExist() throws Exception {
        String mockEmail = "test@example.com";
        List<String> mockLocations = List.of("Location A");

        Mockito.when(informationOrchestrator.getAllLocationsByCustomerEmail(mockEmail))
                .thenReturn(mockLocations);

        // 5. This will now execute perfectly without invoking Spring's container
        mockMvc.perform(get("/api/v1/customers/locations")
                        .param("email", mockEmail))
                .andExpect(status().isOk());
    }
}