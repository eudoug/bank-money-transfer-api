package com.doug.moneytransferapi.data.support

import com.doug.moneytransferapi.data.AccountDataObject
import com.doug.moneytransferapi.data.DataFactory
import com.doug.moneytransferapi.exception.ExceptionHandler
import com.doug.moneytransferapi.model.Account
import com.doug.moneytransferapi.model.CustomerTransaction
import com.doug.moneytransferapi.model.MoneyTransaction
import org.apache.commons.dbutils.DbUtils
import org.apache.log4j.Logger
import java.math.BigDecimal
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.ArrayList

class AccountSupport : AccountDataObject {
    /**
     * Get all accounts.
     */
    override val allAccounts: List<Account>
        @Throws(ExceptionHandler::class)
        get() {
            var conn: Connection? = null
            var stmt: PreparedStatement? = null
            var rs: ResultSet? = null
            val allAccounts = ArrayList<Account>()
            try {
                conn = DataFactory.getConnection()
                stmt = conn.prepareStatement(SQL_GET_ALL_ACC)
                rs = stmt.run { executeQuery() }
                while (rs.run { next() }) {
                    val acc = Account(
                        rs.getLong("AccountId"), rs.getString("CustomerName"),
                        rs.getBigDecimal("Balance"), rs.getString("CurrencyCode")
                    )
                    if (log.isDebugEnabled)
                        log.debug("getAllAccounts(): Get  Account $acc")
                    allAccounts.add(acc)
                }
                return allAccounts
            } catch (e: SQLException) {
                throw ExceptionHandler("getAccountById(): Error reading account data", e)
            } finally {
                DbUtils.closeQuietly(conn, stmt, rs)
            }
        }

    /**
     * Get account by id
     */
    @Throws(ExceptionHandler::class)
    override fun getAccountById(accountId: Long): Account {
        var conn: Connection? = null
        var stmt: PreparedStatement? = null
        var rs: ResultSet? = null
        var acc: Account? = null
        try {
            conn = DataFactory.getConnection()
            stmt = conn.prepareStatement(SQL_GET_ACC_BY_ID)
            stmt.setLong(1, accountId)
            rs = stmt.executeQuery()
            if (rs.run { next() }) {
                acc = Account(
                    rs.getLong("AccountId"), rs.getString("CustomerName"), rs.getBigDecimal("Balance"),
                    rs.getString("CurrencyCode")
                )
                if (log.isDebugEnabled)
                    log.debug("Retrieve Account By Id: $acc")
            }
            return acc!!
        } catch (e: SQLException) {
            throw ExceptionHandler("getAccountById(): Error reading account data", e)
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs)
        }

    }

    /**
     * Create account
     */
    @Throws(ExceptionHandler::class)
    override fun createAccount(account: Account): Long {
        var conn: Connection? = null
        var stmt: PreparedStatement? = null
        var generatedKeys: ResultSet? = null
        try {
            conn = DataFactory.getConnection()
            stmt = conn.prepareStatement(SQL_CREATE_ACC)
            stmt.setString(1, account.customerName)
            stmt.setBigDecimal(2, account.balance)
            stmt.setString(3, account.currencyCode)
            val affectedRows = stmt.executeUpdate()
            if (affectedRows == 0) {
                log.error("createAccount(): Creating account failed, no rows affected.")
                throw ExceptionHandler("Account Cannot be created")
            }
            generatedKeys = stmt.generatedKeys
            if (generatedKeys!!.next()) {
                return generatedKeys.getLong(1)
            } else {
                log.error("Creating account failed, no ID obtained.")
                throw ExceptionHandler("Account Cannot be created")
            }
        } catch (e: SQLException) {
            log.error("Error Inserting Account  $account")
            throw ExceptionHandler("createAccount(): Error creating customer account $account", e)
        } finally {
            DbUtils.closeQuietly(conn, stmt, generatedKeys)
        }
    }

    /**
     * Delete account by id
     */
    @Throws(ExceptionHandler::class)
    override fun deleteAccountById(accountId: Long): Int {
        lateinit var conn: Connection
        lateinit var stmt: PreparedStatement
        try {
            conn = DataFactory.getConnection()
            stmt = conn.prepareStatement(SQL_DELETE_ACC_BY_ID)
            stmt.setLong(1, accountId)
            return stmt.executeUpdate()
        } catch (e: SQLException) {
            throw ExceptionHandler("deleteAccountById(): Error deleting customer account Id $accountId", e)
        } finally {
            DbUtils.closeQuietly(conn)
            DbUtils.closeQuietly(stmt)
        }
    }

    /**
     * Update account balance
     */
    @Throws(ExceptionHandler::class)
    override fun updateAccountBalance(accountId: Long, deltaAmount: BigDecimal): Int {
        lateinit var conn: Connection
        lateinit var lockStmt: PreparedStatement
        lateinit var updateStmt: PreparedStatement
        lateinit var rs: ResultSet
        lateinit var targetAccount: Account
        var updateCount = -1
        try {
            conn = DataFactory.getConnection()
            conn.autoCommit = false
            // lock account for writing:
            lockStmt = conn.prepareStatement(SQL_LOCK_ACC_BY_ID)
            lockStmt.setLong(1, accountId)
            rs = lockStmt.executeQuery()
            if (rs.run { next() }) {
                targetAccount = Account(
                    rs.getLong("AccountId"), rs.getString("CustomerName"),
                    rs.getBigDecimal("Balance"), rs.getString("CurrencyCode")
                )
                if (log.isDebugEnabled)
                    log.debug("updateAccountBalance from Account: $targetAccount")
            }

            // update account upon success locking
            val balance = targetAccount.balance?.add(deltaAmount)
            if (balance!! < MoneyTransaction.zeroAmount) {
                throw ExceptionHandler("Not sufficient Fund for account: $accountId")
            }

            updateStmt = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE)
            updateStmt.setBigDecimal(1, balance)
            updateStmt.setLong(2, accountId)
            updateCount = updateStmt.executeUpdate()
            conn.commit()
            if (log.isDebugEnabled)
                log.debug("New Balance after Update: $targetAccount")
            return updateCount
        } catch (se: SQLException) {
            // rollback transaction if exception occurs
            log.error("updateAccountBalance(): Customer Transaction Failed, rollback initiated for: $accountId", se)
            try {
                conn.rollback()
            } catch (re: SQLException) {
                throw ExceptionHandler("Fail to rollback transaction", re)
            }

        } finally {
            DbUtils.closeQuietly(conn)
            DbUtils.closeQuietly(rs)
            DbUtils.closeQuietly(lockStmt)
            DbUtils.closeQuietly(updateStmt)
        }
        return updateCount
    }

    /**
     * Transfer balance between two accounts.
     */
    @Throws(ExceptionHandler::class)
    override fun transferAccountBalance(customerTransaction: CustomerTransaction): Int {
        var result = -1
        lateinit var conn: Connection
        lateinit var lockStmt: PreparedStatement
        lateinit var updateStmt: PreparedStatement
        lateinit var rs: ResultSet
        lateinit var fromAccount: Account
        lateinit var toAccount: Account

        try {
            conn = DataFactory.getConnection()
            conn.autoCommit = false
            // lock the credit and debit account for writing:
            lockStmt = conn.prepareStatement(SQL_LOCK_ACC_BY_ID)
            lockStmt.setLong(1, customerTransaction.fromAccountId!!)
            rs = lockStmt.executeQuery()
            if (rs.run { next() }) {
                fromAccount = Account(
                    rs.getLong("AccountId"), rs.getString("CustomerName"),
                    rs.getBigDecimal("Balance"), rs.getString("CurrencyCode")
                )
                if (log.isDebugEnabled)
                    log.debug("""transferAccountBalance from Account: $fromAccount""")
            }
            lockStmt = conn.prepareStatement(SQL_LOCK_ACC_BY_ID)
            lockStmt.setLong(1, customerTransaction.toAccountId!!)
            rs = lockStmt.executeQuery()
            if (rs.next()) {
                toAccount = Account(
                    rs.getLong("AccountId"), rs.getString("CustomerName"), rs.getBigDecimal("Balance"),
                    rs.getString("CurrencyCode")
                )
                if (log.isDebugEnabled)
                    log.debug("""transferAccountBalance to Account: $toAccount""")
            }

            // check transaction currency
            if (fromAccount.currencyCode != customerTransaction.currencyCode) {
                throw ExceptionHandler(
                    "Fail to transfer Fund, transaction ccy are different from source/destination"
                )
            }

            // check ccy is the same for both accounts
            if (fromAccount.currencyCode != toAccount.currencyCode) {
                throw ExceptionHandler(
                    "Fail to transfer Fund, the source and destination account are in different currency"
                )
            }

            // check enough fund in source account
            val fromAccountLeftOver = fromAccount.balance?.subtract(customerTransaction.amount)
            if (fromAccountLeftOver?.compareTo(MoneyTransaction.zeroAmount)!! < 0) {
                throw ExceptionHandler("Not enough Fund from source Account ")
            }

            // proceed with update
            updateStmt = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE)
            updateStmt.setBigDecimal(1, fromAccountLeftOver)
            updateStmt.setLong(2, customerTransaction.fromAccountId!!)
            updateStmt.addBatch()
            updateStmt.setBigDecimal(1, toAccount.balance?.add(customerTransaction.amount))
            updateStmt.setLong(2, customerTransaction.toAccountId!!)
            updateStmt.addBatch()
            val rowsUpdated = updateStmt.executeBatch()
            result = rowsUpdated[0] + rowsUpdated[1]
            if (log.isDebugEnabled) {
                log.debug("Number of rows updated for the transfer : $result")
            }
            // If there is no error, commit the transaction
            conn.commit()
        } catch (se: SQLException) {
            // rollback transaction if exception occurs
            log.error(
                "transferAccountBalance(): Customer Transaction Failed, rollback initiated for: $customerTransaction",
                se
            )
            try {
                conn.rollback()
            } catch (re: SQLException) {
                throw ExceptionHandler("Fail to rollback transaction", re)
            }

        } finally {
            DbUtils.closeQuietly(conn)
            DbUtils.closeQuietly(rs)
            DbUtils.closeQuietly(lockStmt)
            DbUtils.closeQuietly(updateStmt)
        }
        return result
    }

    companion object {

        private val log = Logger.getLogger(AccountSupport::class.java)
        private const val SQL_GET_ACC_BY_ID = "SELECT * FROM Account WHERE AccountId = ? "
        private const val SQL_LOCK_ACC_BY_ID = "SELECT * FROM Account WHERE AccountId = ? FOR UPDATE"
        private const val SQL_CREATE_ACC = "INSERT INTO Account (CustomerName, Balance, CurrencyCode) VALUES (?, ?, ?)"
        private const val SQL_UPDATE_ACC_BALANCE = "UPDATE Account SET Balance = ? WHERE AccountId = ? "
        private const val SQL_GET_ALL_ACC = "SELECT * FROM Account"
        private const val SQL_DELETE_ACC_BY_ID = "DELETE FROM Account WHERE AccountId = ?"
    }

}