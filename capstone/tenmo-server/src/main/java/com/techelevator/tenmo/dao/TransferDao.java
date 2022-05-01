package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.util.List;

    public interface TransferDao {

        List<Transfer> findAll();

        List<Transfer> getAllTransfersById(int transferId);

        Transfer getATransferById(int transferId);

        List<Transfer>getAllTransfersByUserId(int userId);

        Transfer updateTransfer(Transfer transfer);}



