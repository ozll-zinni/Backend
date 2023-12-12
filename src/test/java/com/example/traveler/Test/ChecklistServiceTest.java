package com.example.traveler.Test;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.ChecklistResponse;
import com.example.traveler.model.entity.ChecklistEntity;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.repository.ChecklistRepository;
import com.example.traveler.repository.TravelRepository;
import com.example.traveler.service.ChecklistService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ChecklistServiceTest {

    @InjectMocks
    private ChecklistService checklistService;

    @Mock
    private TravelRepository travelRepository;

    @Mock
    private ChecklistRepository checklistRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveChecklist() throws BaseException {
        Travel mockTravel = new Travel();
        mockTravel.setTId(1);

        ChecklistEntity mockChecklist = new ChecklistEntity();
        mockChecklist.setCId(1);
        mockChecklist.setTitle("새로운 체크리스트");
        mockChecklist.setTravel(mockTravel);

        when(travelRepository.findById(1)).thenReturn(Optional.of(mockTravel));

        when(checklistRepository.save(any(ChecklistEntity.class))).thenReturn(mockChecklist);

        ChecklistResponse response = checklistService.saveChecklist("accessToken", 1);

        assertNotNull(response);
        assertEquals(1, response.getCId());
        assertEquals("새로운 체크리스트", response.getTitle());
        assertEquals(1, response.getTId());
    }

    @Test
    public void testGetChecklist() throws BaseException {
        ChecklistEntity mockChecklist = new ChecklistEntity();
        mockChecklist.setCId(1);
        mockChecklist.setTitle("체크리스트 1");

        when(checklistRepository.findBycIdAndState(1, 1)).thenReturn(Optional.of(mockChecklist));

        ChecklistResponse response = checklistService.getChecklist(1);

        assertNotNull(response);
        assertEquals(1, response.getCId());
        assertEquals("체크리스트 1", response.getTitle());
    }

    @Test
    public void testPatchChecklist() throws BaseException {
        ChecklistEntity mockChecklist = new ChecklistEntity();
        mockChecklist.setCId(1);
        mockChecklist.setTitle("체크리스트 1");

        when(checklistRepository.findBycId(1)).thenReturn(Optional.of(mockChecklist));
        when(checklistRepository.save(any(ChecklistEntity.class))).thenReturn(mockChecklist);

        ChecklistResponse response = checklistService.patchChecklist("accessToken", 1, "수정된 체크리스트 1");

        assertNotNull(response);
        assertEquals(1, response.getCId());
        assertEquals("수정된 체크리스트 1", response.getTitle());
    }

    @Test
    public void testDeleteChecklist() throws BaseException {
        ChecklistEntity mockChecklist = new ChecklistEntity();
        mockChecklist.setCId(1);

        when(checklistRepository.findById(1)).thenReturn(Optional.of(mockChecklist));
        doNothing().when(checklistRepository).delete(mockChecklist);

        int result = checklistService.deleteChecklist("accessToken", 1);

        assertEquals(1, result);
    }

    @Test
    public void testGetAllChecklistsByTravel() throws BaseException {
        Travel mockTravel = new Travel();
        mockTravel.setTId(1);

        ChecklistEntity checklist1 = new ChecklistEntity();
        checklist1.setCId(1);
        checklist1.setTitle("체크리스트 1");
        checklist1.setTravel(mockTravel);

        ChecklistEntity checklist2 = new ChecklistEntity();
        checklist2.setCId(2);
        checklist2.setTitle("체크리스트 2");
        checklist2.setTravel(mockTravel);

        when(travelRepository.findById(1)).thenReturn(Optional.of(mockTravel));

        when(checklistRepository.findAllByTravel(mockTravel)).thenReturn(Arrays.asList(checklist1, checklist2));

        List<ChecklistResponse> responses = checklistService.getAllChecklistsByTravel(1);

        assertNotNull(responses);
        assertEquals(2, responses.size());

        ChecklistResponse response1 = responses.get(0);
        assertEquals(1, response1.getCId());
        assertEquals("체크리스트 1", response1.getTitle());
        assertEquals(1, response1.getTId());

        ChecklistResponse response2 = responses.get(1);
        assertEquals(2, response2.getCId());
        assertEquals("체크리스트 2", response2.getTitle());
        assertEquals(1, response2.getTId());
    }
}

