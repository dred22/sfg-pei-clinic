package guru.springframework.sfgpetclinic.service;

import guru.springframework.sfgpetclinic.model.Owner;

import java.util.Set;

interface OwnerService {

    Owner findByLastName(String lastName);

    Owner findById(Long id);

    Owner sava(Owner owner);

    Set<Owner> findAll();

}
