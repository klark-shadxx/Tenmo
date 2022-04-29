package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

    public interface TransferDao {

        List<Transfer> getAllTransfersById(int transferId);

        Transfer getATransferById(int id);


        Transfer updateTransfer(Transfer transfer);}



