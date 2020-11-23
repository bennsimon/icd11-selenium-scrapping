package Restapi.Service;

import Restapi.Model.Variation;

import java.util.List;

public interface CollectionService {
    void startCollection() throws InterruptedException;

    List<Variation> findAllByNameOrCode(String name, String code);
}


