package guru.springframework.sfgpetclinic.model;

import java.time.LocalTime;

public class Pet {

    private PetType petType;
    private Owner owner;
    private LocalTime birthDay;

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public LocalTime getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalTime birthDay) {
        this.birthDay = birthDay;
    }
}
