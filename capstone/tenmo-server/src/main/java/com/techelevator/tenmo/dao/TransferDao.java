package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.List;

    public interface TransferDao {

        List<Transfer> findAll();

        List<Transfer> getAllTransfersById(int transferId);

        Transfer getATransferById(int id);

        List<Transfer>getAllTransfersByUserId(long userId);

        Transfer updateTransfer(Transfer transfer);}



