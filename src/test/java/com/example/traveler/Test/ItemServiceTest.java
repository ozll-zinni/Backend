package com.example.traveler.Test;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.ItemNameRequest;
import com.example.traveler.model.dto.ItemResponse;
import com.example.traveler.model.entity.ChecklistEntity;
import com.example.traveler.model.entity.ItemEntity;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.ChecklistRepository;
import com.example.traveler.repository.ItemRepository;
import com.example.traveler.service.ItemService;
import com.example.traveler.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.traveler.config.BaseResponseStatus.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ChecklistRepository checklistRepository;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveItem() throws BaseException {
        User mockUser = new User();

        ChecklistEntity mockChecklist = new ChecklistEntity();
        mockChecklist.setCId(1);

        ItemEntity mockItem = new ItemEntity();
        mockItem.setId(1L);
        mockItem.setName("새로운 준비물");
        mockItem.setIschecked(false);
        mockItem.setChecklist(mockChecklist);

        when(userService.getUserByToken("accessToken")).thenReturn(mockUser);

        when(checklistRepository.findById(1)).thenReturn(Optional.of(mockChecklist));

        when(itemRepository.save(any(ItemEntity.class))).thenReturn(mockItem);

        ItemResponse response = itemService.saveItem("accessToken", 1);

        assertNotNull(response);
        assertEquals(Optional.of(1), response.getIId());
        assertEquals("새로운 준비물", response.getName());
        assertFalse(response.getIsChecked());
        assertEquals(1, response.getCId());
    }

    @Test
    public void testGetAllItem() {
        ItemEntity item1 = new ItemEntity();
        item1.setId(1L);
        item1.setName("아이템 1");
        item1.setIschecked(false);

        ItemEntity item2 = new ItemEntity();
        item2.setId(2L);
        item2.setName("아이템 2");
        item2.setIschecked(true);

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        List<ItemResponse> responses = itemService.getAllItem();

        assertNotNull(responses);
        assertEquals(2, responses.size());

        ItemResponse response1 = responses.get(0);
        assertEquals(Optional.of(1), response1.getIId());
        assertEquals("아이템 1", response1.getName());
        assertFalse(response1.getIsChecked());

        ItemResponse response2 = responses.get(1);
        assertEquals(Optional.of(2), response2.getIId());
        assertEquals("아이템 2", response2.getName());
        assertTrue(response2.getIsChecked());
    }

    @Test
    public void testGetItem() throws BaseException {
        ItemEntity mockItem = new ItemEntity();
        mockItem.setId(1L);
        mockItem.setName("아이템 1");
        mockItem.setIschecked(false);

        when(itemRepository.findById(1)).thenReturn(Optional.of(mockItem));

        ItemResponse response = itemService.getItem(1);

        assertNotNull(response);
        assertEquals(Optional.of(1), response.getIId());
        assertEquals("아이템 1", response.getName());
        assertFalse(response.getIsChecked());
    }

    @Test
    public void testGetAllItemByChecklist() throws BaseException {
        ChecklistEntity mockChecklist = new ChecklistEntity();
        mockChecklist.setCId(1);

        ItemEntity item1 = new ItemEntity();
        item1.setId(1L);
        item1.setName("아이템 1");
        item1.setIschecked(false);
        item1.setChecklist(mockChecklist);

        ItemEntity item2 = new ItemEntity();
        item2.setId(2L);
        item2.setName("아이템 2");
        item2.setIschecked(true);
        item2.setChecklist(mockChecklist);

        when(checklistRepository.findById(1)).thenReturn(Optional.of(mockChecklist));

        when(itemRepository.findAllByChecklist(mockChecklist)).thenReturn(Arrays.asList(item1, item2));

        List<ItemResponse> responses = itemService.getallItemByChecklist(1);

        assertNotNull(responses);
        assertEquals(2, responses.size());

        ItemResponse response1 = responses.get(0);
        assertEquals(Optional.of(1), response1.getIId());
        assertEquals("아이템 1", response1.getName());
        assertFalse(response1.getIsChecked());

        ItemResponse response2 = responses.get(1);
        assertEquals(Optional.of(2), response2.getIId());
        assertEquals("아이템 2", response2.getName());
        assertTrue(response2.getIsChecked());
    }

    @Test
    public void testPatchItem() throws BaseException {
        ItemEntity mockItem = new ItemEntity();
        mockItem.setId(1L);
        mockItem.setName("아이템 1");
        mockItem.setIschecked(false);

        when(userService.getUserByToken("accessToken")).thenReturn(new User());

        when(itemRepository.findByIdAndChecklist_cId(1, 1)).thenReturn(Optional.of(mockItem));
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(mockItem);

        ItemResponse response = itemService.patchItem("accessToken", 1, 1);

        assertNotNull(response);
        assertEquals(Optional.of(1), response.getIId());
        assertEquals("아이템 1", response.getName());
        assertTrue(response.getIsChecked()); // 토글됨
    }

    @Test
    public void testPatchItemName() throws BaseException {
        ItemEntity mockItem = new ItemEntity();
        mockItem.setId(1L);
        mockItem.setName("아이템 1");
        mockItem.setIschecked(false);

        when(userService.getUserByToken("accessToken")).thenReturn(new User());

        when(itemRepository.findByIdAndChecklist_cId(1, 1)).thenReturn(Optional.of(mockItem));
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(mockItem);

        ItemNameRequest itemNameRequest = new ItemNameRequest("새로운 아이템 이름");
        ItemResponse response = itemService.patchItemName("accessToken", 1, 1, itemNameRequest);

        assertNotNull(response);
        assertEquals(Optional.of(1), response.getIId());
        assertEquals("새로운 아이템 이름", response.getName());
        assertFalse(response.getIsChecked());
    }

    @Test
    public void testDeleteItem() throws BaseException {
        ItemEntity mockItem = new ItemEntity();
        mockItem.setId(1L);
        mockItem.setName("아이템 1");
        mockItem.setIschecked(false);

        when(userService.getUserByToken("accessToken")).thenReturn(new User());

        when(itemRepository.findByIdAndChecklist_cId(1, 1)).thenReturn(Optional.of(mockItem));
        doNothing().when(itemRepository).delete(mockItem);

        int cId = itemService.deleteItem("accessToken", 1, 1);

        assertEquals(1, cId);
    }

    @Test
    public void testGetItemFromChecklist() throws BaseException {
        ChecklistEntity mockChecklist = new ChecklistEntity();
        mockChecklist.setCId(1);

        ItemEntity mockItem = new ItemEntity();
        mockItem.setId(1L);
        mockItem.setName("아이템 1");
        mockItem.setIschecked(false);
        mockItem.setChecklist(mockChecklist);

        when(checklistRepository.findById(1)).thenReturn(Optional.of(mockChecklist));

        when(itemRepository.findByIdAndChecklist(1, mockChecklist)).thenReturn(Optional.of(mockItem));
        doNothing().when(itemRepository).delete(mockItem);

        ItemResponse response = itemService.getItemFromChecklist(1, 1);

        assertNotNull(response);
        assertEquals(Optional.of(1), response.getIId());
        assertEquals("아이템 1", response.getName());
        assertFalse(response.getIsChecked());
    }
}
