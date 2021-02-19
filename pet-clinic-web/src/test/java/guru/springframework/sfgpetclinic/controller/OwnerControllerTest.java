package guru.springframework.sfgpetclinic.controller;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.service.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static guru.springframework.sfgpetclinic.controller.OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    OwnerService ownerService;

    @InjectMocks
    OwnerController controller;

    Set<Owner> owners;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        owners = new HashSet<>();
        owners.add(Owner.builder().id(1l).build());
        owners.add(Owner.builder().id(2l).build());

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }
    @Test
    void initCreationForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists("owner"));
    }

    @Test
    void processCreationForm() throws Exception {
        when(ownerService.save(ArgumentMatchers.any())).thenReturn(Owner.builder().id(1L).build());

        mockMvc.perform(MockMvcRequestBuilders.post("/owners/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"))
                .andExpect(model().attributeExists("owner"));
    }

    @Test
    void initUpdateForm() throws Exception {
        when(ownerService.findById(1L)).thenReturn(Owner.builder().id(1L).build());
        mockMvc.perform(MockMvcRequestBuilders.get("/owners/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM))
                .andExpect(model().attributeExists("owner"));
        verify(ownerService).findById(1L);
    }

    @Test
    void processUpdateForm() throws Exception {
        when(ownerService.save(ArgumentMatchers.any())).thenReturn(Owner.builder().id(1L).build());

        mockMvc.perform(MockMvcRequestBuilders.post("/owners/1/edit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"))
                .andExpect(model().attributeExists("owner"));
        verify(ownerService).save(ArgumentMatchers.any());
    }

    @Test
    void listOwnersByIndex() throws Exception {
        when(ownerService.findAll()).thenReturn(owners);

        mockMvc.perform(MockMvcRequestBuilders.get("/owners/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/index"))
                .andExpect(model().attribute("owners", hasSize(2)));
    }

    @Test
    void findOwners() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"))
                .andExpect(model().attributeExists("owner"));

        verifyNoInteractions(ownerService);
    }
    @Test
    void processFindFormReturnEmpty() throws Exception {
        when(ownerService.findAllByLastNameContainingIgnoreCase(anyString())).thenReturn(Collections.emptySet());
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
        //.andExpect(model().attributeExists("selections"));

        verify(ownerService).findAllByLastNameContainingIgnoreCase(anyString());
    }
    @Test
    void processFindFormReturnMany() throws Exception {
        when(ownerService.findAllByLastNameContainingIgnoreCase(anyString())).thenReturn(owners);
        mockMvc.perform(MockMvcRequestBuilders.get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attributeExists("selections"));

        verify(ownerService).findAllByLastNameContainingIgnoreCase(anyString());
    }
    @Test
    void processFindFormReturnOne() throws Exception {
        when(ownerService.findAllByLastNameContainingIgnoreCase(anyString())).thenReturn(Set.of(Owner.builder().id(1L).build()));
        mockMvc.perform(MockMvcRequestBuilders.get("/owners"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"))
                .andExpect(model().attributeExists("owner"));

        verify(ownerService).findAllByLastNameContainingIgnoreCase(anyString());
    }



    @Test
    void showOwner() throws Exception {
        Owner owner = Owner.builder().id(1L).build();
        when(ownerService.findById(1L)).thenReturn(owner);
        mockMvc.perform(MockMvcRequestBuilders.get("/owners/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"))
                .andExpect(model().attribute("owner", owner));

        verify(ownerService).findById(1L);
    }
}