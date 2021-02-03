package guru.springframework.sfgpetclinic.service;

import guru.springframework.sfgpetclinic.model.Owner;

interface OwnerService extends CrudService<Owner, Long> {

    Owner findByLastName(String lastName);

}
