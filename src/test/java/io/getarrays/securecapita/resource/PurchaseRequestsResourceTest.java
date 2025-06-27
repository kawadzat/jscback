package io.getarrays.securecapita.resource;

import io.getarrays.securecapita.domain.PurchaseRequisition;
import io.getarrays.securecapita.service.EmailService;
import io.getarrays.securecapita.service.PurchaseRequisitionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
/**
 * @author kunal
 * @project securecapita7
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(PurchaseRequisitionResource.class)
class PurchaseRequestsResourceTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PurchaseRequisitionService mockPurchaseRequestsService;
    @MockBean
    private EmailService mockEmailService;
    @Test
    void testCreatePurchase() throws Exception {
        final PurchaseRequisition purchaseRequests = PurchaseRequisition.builder()
                .id(0L)
                .receiverEmail("kkunal2005@gmail.com")
                .build();
        when(mockPurchaseRequestsService.createPurchaseRequest(PurchaseRequisition.builder()
                .id(0L)
                .receiverEmail("kkunal2005@gmail.com")
                .build())).thenReturn(purchaseRequests);

        final MockHttpServletResponse response = mockMvc.perform(post("/purcharserequetsts/create")
                        .content("").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("kkunal2005@gmail.com");
//        verify(mockEmailService).sendEmail("kkunal2005@gmail.com", "Purchase Request Email Verification Sent By Kumar Kunal",
//                "message");
    }

    @Test
    void testFindById() throws Exception {
        final PurchaseRequisition purchaseRequests = PurchaseRequisition.builder()
                .id(0L)
                .receiverEmail("tendaikawadza49@gmail.com")
                .build();
        when(mockPurchaseRequestsService.getPurchaseRequestById(0L)).thenReturn(purchaseRequests);

        final MockHttpServletResponse response = mockMvc.perform(get("/purcharserequetsts/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("tendaikawadza49@gmail.com");
    }

    @Test
    void testFindAll() throws Exception {
        final List<PurchaseRequisition> purchaseRequests = List.of(PurchaseRequisition.builder()
                .id(0L)
                .receiverEmail("kkunal2005@gmail.com")
                .build());
        when(mockPurchaseRequestsService.getAllPurchaseRequests()).thenReturn(purchaseRequests);
        final MockHttpServletResponse response = mockMvc.perform(get("/purcharserequetsts/")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("kkunal2005@gmail.com");
    }

    @Test
    void testFindAll_PurchaseRequestsServiceReturnsNoItems() throws Exception {
        when(mockPurchaseRequestsService.getAllPurchaseRequests()).thenReturn(Collections.emptyList());
        final MockHttpServletResponse response = mockMvc.perform(get("/purcharserequetsts/")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }
}
