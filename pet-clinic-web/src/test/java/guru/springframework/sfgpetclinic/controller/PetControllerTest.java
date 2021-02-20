package guru.springframework.sfgpetclinic.controller;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.service.OwnerService;
import guru.springframework.sfgpetclinic.service.PetService;
import guru.springframework.sfgpetclinic.service.PetTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static guru.springframework.sfgpetclinic.controller.PetController.VIEWS_PETS_CREATE_OR_UPDATE_FORM;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {
    @Mock
    private PetService petService;
    @Mock
    private PetTypeService petTypeService;
    @Mock
    private OwnerService ownerService;
    @InjectMocks
    private PetController petController;

    MockMvc mockMvc;
    Owner owner;
    Set<PetType> petTypes;
    Pet pet;

    @BeforeEach
    void setUp() {
        owner = Owner.builder().id(1L).build();
        pet = Pet.builder().id(1L).name("Pet").build();
        petTypes = new HashSet<>();
        petTypes.add(PetType.builder().id(1L).name("Dog").build());
        petTypes.add(PetType.builder().id(2L).name("Cat").build());

        mockMvc = MockMvcBuilders
                .standaloneSetup(petController)
                .build();
    }

    @Test
    void initCreationForm() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);

        mockMvc.perform(MockMvcRequestBuilders.get("/owners/1/pets/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("pet"))
                .andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
    }

    @Test
    void processCreationForm() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);
        when(petService.save(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(MockMvcRequestBuilders.post("/owners/1/pets/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));

    }

    @Test
    void initUpdateForm() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner);
        when(petService.findById(anyLong())).thenReturn(pet);
        when(petTypeService.findAll()).thenReturn(petTypes);

        mockMvc.perform(MockMvcRequestBuilders.get("/owners/1/pets/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("pet"))
                .andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
    }

    @Test
    void processUpdateForm() throws Exception {
        when(ownerService.findById(anyLong())).thenReturn(owner);
        when(petTypeService.findAll()).thenReturn(petTypes);

        mockMvc.perform(MockMvcRequestBuilders.post("/owners/1/pets/1/edit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"))
                .andExpect(model().attributeExists("owner"));
        verify(petService).save(ArgumentMatchers.any());
    }
}