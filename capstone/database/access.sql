SELECT account.balance FROM account JOIN tenmo_user 
ON tenmo_user.user_id = account.user_id
WHERE user_id= 1000;

SELECT*FROM tenmo_user;

SELECT*FROM account JOIN transfer 
ON transfer.user_id = trans.user_id
WHERE user_id = 1001;

--transfer history
SELECT*FROM transfer 
JOIN account



-- account
SELECT transfer_id, transfer_type_id, account_from, account_to, amount, account_id,  user_id, balance
FROM transfer
Join account ON transfer.account_to = account.account_id;

SELECT transfer_id, transfer_type_id, account_from, account_to, amount, account_id,  user_id, balance
FROM transfer
Join account ON transfer.account_from = account.account_id 
Where account_id = '1001';

SELECT*FROM transfer_status;