package com.epam.todo.guid.controller;

import com.epam.common.core.dto.SingleResponse;
import com.epam.todo.guid.service.IUidGenerateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuidControllerTest {

    @InjectMocks
    private GuidController guidController;

    @Mock
    private IUidGenerateService uidGenerateService;

    @Test
    void testGetUid() {
        long mockReturnUid = new Random().nextLong();
        when(uidGenerateService.getUid()).thenReturn(mockReturnUid);
        SingleResponse<Long> singleUid = guidController.getUid();
        assertTrue(singleUid.isSuccess());
        assertEquals(mockReturnUid, singleUid.getData());
    }

    @Test
    void testBatchIds() {
        int shouldReturnUidCount = 10;
        List<Long> mockReturnUidArray = new ArrayList<>(shouldReturnUidCount);
        for (int i = 0; i < shouldReturnUidCount; i++) {
            mockReturnUidArray.add(new Random().nextLong());
        }

        when(uidGenerateService.batchIds(shouldReturnUidCount)).thenReturn(mockReturnUidArray);
        assertEquals(mockReturnUidArray, guidController.batchIds(shouldReturnUidCount).getData());
    }
}
