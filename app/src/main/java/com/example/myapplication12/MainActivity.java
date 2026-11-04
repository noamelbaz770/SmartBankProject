package com.example.myapplication12;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private BankManager bankManager;
    private BankAccount currentAccount;

    private TextView tvAccountDetails, tvConsole;
    private TextInputEditText etAmount, etTargetAccount;
    private Button btnAcc1, btnAcc2, btnAcc3;
    private Button btnDeposit, btnWithdraw, btnTransfer;
    private Button btnTestArithmetic, btnTestNumberFormat, btnTestNullPointer, btnTestArrayIndex, btnTestIllegalArgument, btnTestInsufficient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bankManager = new BankManager();
        currentAccount = bankManager.getAccounts().get(0); // Default to ACC-001

        initViews();
        setupListeners();
        updateAccountUI("Welcome to Smart Bank System!");
    }

    private void initViews() {
        tvAccountDetails = findViewById(R.id.tvAccountDetails);
        tvConsole = findViewById(R.id.tvConsole);
        etAmount = findViewById(R.id.etAmount);
        etTargetAccount = findViewById(R.id.etTargetAccount);

        btnAcc1 = findViewById(R.id.btnAcc1);
        btnAcc2 = findViewById(R.id.btnAcc2);
        btnAcc3 = findViewById(R.id.btnAcc3);

        btnDeposit = findViewById(R.id.btnDeposit);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnTransfer = findViewById(R.id.btnTransfer);

        btnTestArithmetic = findViewById(R.id.btnTestArithmetic);
        btnTestNumberFormat = findViewById(R.id.btnTestNumberFormat);
        btnTestNullPointer = findViewById(R.id.btnTestNullPointer);
        btnTestArrayIndex = findViewById(R.id.btnTestArrayIndex);
        btnTestIllegalArgument = findViewById(R.id.btnTestIllegalArgument);
        btnTestInsufficient = findViewById(R.id.btnTestInsufficient);
    }

    private void setupListeners() {
        btnAcc1.setOnClickListener(v -> selectAccount("ACC-001"));
        btnAcc2.setOnClickListener(v -> selectAccount("ACC-002"));
        btnAcc3.setOnClickListener(v -> selectAccount("ACC-003"));

        btnDeposit.setOnClickListener(v -> performDeposit());
        btnWithdraw.setOnClickListener(v -> performWithdrawal());
        btnTransfer.setOnClickListener(v -> performTransfer());

        btnTestArithmetic.setOnClickListener(v -> {
            String result = bankManager.testArithmeticException(10, 0);
            logToConsole(result);
        });

        btnTestNumberFormat.setOnClickListener(v -> {
            String result = bankManager.testNumberFormatException("abc_not_a_number");
            logToConsole(result);
        });

        btnTestNullPointer.setOnClickListener(v -> {
            String result = bankManager.testNullPointerException();
            logToConsole(result);
        });

        btnTestArrayIndex.setOnClickListener(v -> {
            String result = bankManager.testArrayIndexOutOfBoundsException();
            logToConsole(result);
        });

        btnTestIllegalArgument.setOnClickListener(v -> {
            String result = bankManager.testIllegalArgumentException();
            logToConsole(result);
        });

        btnTestInsufficient.setOnClickListener(v -> {
            try {
                // Attempt to withdraw an excessive amount to trigger InsufficientFundsException
                currentAccount.withdraw(currentAccount.getBalance() + 1000.0);
            } catch (InsufficientFundsException | InvalidAmountException e) {
                logToConsole("Caught InsufficientFundsException: " + e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                System.out.println("InsufficientFundsException test finally block executed.");
            }
        });
    }

    private void selectAccount(String accNum) {
        BankAccount acc = bankManager.findAccount(accNum);
        if (acc != null) {
            currentAccount = acc;
            updateAccountUI("Switched to " + accNum);
        }
    }

    private void updateAccountUI(String message) {
        String details = String.format("Active Account: %s | Owner: %s | Balance: $%.2f",
                currentAccount.getAccountNumber(),
                currentAccount.getOwnerName(),
                currentAccount.getBalance());
        tvAccountDetails.setText(details);
        logToConsole(message);
    }

    private void logToConsole(String message) {
        String currentText = tvConsole.getText().toString();
        String newText = message + "\n" + currentText;
        tvConsole.setText(newText);
    }

    private double parseAmountInput() throws NumberFormatException, InvalidAmountException {
        String input = etAmount.getText().toString().trim();
        if (input.isEmpty()) {
            throw new InvalidAmountException("Amount input cannot be empty.");
        }
        double amount = Double.parseDouble(input); // Can throw NumberFormatException
        return amount;
    }

    private void performDeposit() {
        try {
            double amount = parseAmountInput();
            currentAccount.deposit(amount);
            bankManager.recordTransaction("DEPOSIT", amount, "Deposited to " + currentAccount.getAccountNumber());
            Toast.makeText(this, "Deposit successful!", Toast.LENGTH_SHORT).show();
            updateAccountUI("Deposited $" + amount + " into " + currentAccount.getAccountNumber());
            etAmount.setText("");
        } catch (NumberFormatException e) {
            String msg = "Error: Invalid number format entered in amount field.";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            logToConsole(msg);
        } catch (InvalidAmountException e) {
            String msg = "Error: " + e.getMessage();
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            logToConsole(msg);
        } finally {
            // Demonstrate try-catch-finally usage
            System.out.println("Deposit operation finally block completed.");
        }
    }

    private void performWithdrawal() {
        try {
            double amount = parseAmountInput();
            currentAccount.withdraw(amount);
            bankManager.recordTransaction("WITHDRAW", amount, "Withdrawn from " + currentAccount.getAccountNumber());
            Toast.makeText(this, "Withdrawal successful!", Toast.LENGTH_SHORT).show();
            updateAccountUI("Withdrew $" + amount + " from " + currentAccount.getAccountNumber());
            etAmount.setText("");
        } catch (NumberFormatException e) {
            String msg = "Error: Invalid number format entered.";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            logToConsole(msg);
        } catch (InvalidAmountException | InsufficientFundsException e) {
            String msg = "Error: " + e.getMessage();
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            logToConsole(msg);
        } finally {
            System.out.println("Withdrawal operation finally block completed.");
        }
    }

    private void performTransfer() {
        try {
            double amount = parseAmountInput();
            String targetAccNum = etTargetAccount.getText().toString().trim();
            if (targetAccNum.isEmpty()) {
                throw new IllegalArgumentException("Target account number cannot be empty.");
            }

            BankAccount targetAccount = bankManager.findAccount(targetAccNum);
            if (targetAccount == null) {
                throw new NullPointerException("Target account " + targetAccNum + " does not exist.");
            }

            currentAccount.transfer(targetAccount, amount);
            bankManager.recordTransaction("TRANSFER", amount, "Transferred from " + currentAccount.getAccountNumber() + " to " + targetAccount.getAccountNumber());
            Toast.makeText(this, "Transfer successful!", Toast.LENGTH_SHORT).show();
            updateAccountUI("Transferred $" + amount + " to " + targetAccount.getAccountNumber());
            etAmount.setText("");
            etTargetAccount.setText("");
        } catch (NumberFormatException e) {
            String msg = "Error: Invalid number format for transfer amount.";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            logToConsole(msg);
        } catch (NullPointerException | IllegalArgumentException | InsufficientFundsException | InvalidAmountException e) {
            String msg = "Error: " + e.getMessage();
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            logToConsole(msg);
        } finally {
            System.out.println("Transfer operation finally block completed.");
        }
    }
}
