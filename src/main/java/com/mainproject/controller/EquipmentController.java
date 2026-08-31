package com.mainproject.controller;

import java.util.*;
import com.mainproject.model.Equipment;
import com.mainproject.dao.EquipmentDAO;

/** Controller layer for Equipment. */
public class EquipmentController {

    private final EquipmentDAO equipmentDAO;

    public EquipmentController() {
        this.equipmentDAO = new EquipmentDAO();
    }

    public boolean addEquipment(
            Equipment equipment) {
        return equipmentDAO.addEquipment(equipment);
    }

    public List<Equipment> getAllEquipment() {
        return equipmentDAO.getAllEquipment();
    }

    public List<Equipment> getAllEquipmentForAdmin() {
        return equipmentDAO.getAllEquipment();
    }

    public Equipment getEquipmentById(
            String equipmentId) {
        return equipmentDAO.getEquipmentById(equipmentId);
    }

    public List<Equipment> getEquipmentByOwner(
            String ownerEmail) {
        return equipmentDAO.getEquipmentByOwner(ownerEmail);
    }

    public boolean updateEquipment(
            Equipment equipment) {
        return equipmentDAO.updateEquipment(equipment);
    }

    public boolean deleteEquipment(
            String equipmentId) {
        return equipmentDAO.deleteEquipment(equipmentId);
    }

}