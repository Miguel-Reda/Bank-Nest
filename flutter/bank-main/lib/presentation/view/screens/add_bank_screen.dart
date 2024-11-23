import 'package:bank/presentation/view/screens/show_banks.dart';
import 'package:bank/presentation/view_model/cubit/bank_cubit.dart';
import 'package:bank/presentation/view_model/cubit/bank_state.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:intl/intl.dart'; // Required for date formatting

class AddBankScreen extends StatelessWidget {
  const AddBankScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final formKey = GlobalKey<FormState>();
    return BlocProvider(
      create: (context) => BankCubit(),
      child: BlocConsumer<BankCubit, BankState>(
        listener: (context, state) {
          if (state is successBankState) {
            Navigator.pushAndRemoveUntil(
              context,
              MaterialPageRoute(
                builder: (context) => ShowBankAccounts(),
              ),
              (route) => false,
            );
          }
        },
        builder: (context, state) {
          final cubit = context.read<BankCubit>();

          return Scaffold(
            appBar: AppBar(
              title: const Text('Calculate Interest'),
              actions: [
                IconButton(
                  icon: Icon(Icons.cancel, color: Colors.red),
                  onPressed: () {
                    Navigator.pushAndRemoveUntil(
                      context,
                      MaterialPageRoute(
                        builder: (context) => ShowBankAccounts(),
                      ),
                      (route) => false,
                    );
                  },
                ),
              ],
            ),
            body: SingleChildScrollView(
              child: Padding(
                padding: const EdgeInsets.all(4.0),
                child: Form(
                  key: formKey,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      // Bank Name
                      TextFormField(
                        controller: cubit.bankController,
                        keyboardType: TextInputType.text,
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Please enter the bank name';
                          }
                          return null;
                        },
                        decoration: const InputDecoration(
                          hintText: 'bank name here',
                          labelText: 'bank name here',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 16),

                      // Amount
                      TextFormField(
                        controller: cubit.amountController,
                        keyboardType: TextInputType.number,
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Please enter the amount';
                          }
                          return null;
                        },
                        decoration: const InputDecoration(
                          labelText: 'amount here',
                          hintText: 'amount here',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 16),

                      // Year
                      TextFormField(
                        controller: cubit.yearController,
                        keyboardType: TextInputType.number,
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Please enter the year';
                          }
                          return null;
                        },
                        decoration: const InputDecoration(
                          hintText: 'year here',
                          labelText: 'year here',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 16),

                      // Percent
                      TextFormField(
                        controller: cubit.percentController,
                        keyboardType: TextInputType.number,
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Please enter the percent';
                          }
                          return null;
                        },
                        decoration: const InputDecoration(
                          hintText: 'percent here',
                          labelText: 'percent here',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 16),

                      // Account Creation Date (New Field)
                      TextFormField(
                        controller: cubit.creationDateController,
                        readOnly: true, // Prevent manual text input
                        decoration: const InputDecoration(
                          hintText: 'Select creation date',
                          labelText: 'Account Creation Date',
                          border: OutlineInputBorder(),
                          suffixIcon: Icon(Icons.calendar_today),
                        ),
                        onTap: () async {
                          // Show date picker to select the creation date
                          DateTime? pickedDate = await showDatePicker(
                            context: context,
                            initialDate: DateTime.now(),
                            firstDate: DateTime(2000),
                            lastDate: DateTime(2101),
                          );

                          if (pickedDate != null) {
                            // Format the selected date and update the controller
                            String formattedDate =
                                DateFormat('yyyy-MM-dd').format(pickedDate);
                            cubit.creationDateController.text = formattedDate;
                          }
                        },
                      ),
                      const SizedBox(height: 16),

                      // Add Account Button
                      ElevatedButton(
                        onPressed: () {
                          if (formKey.currentState!.validate()) {
                            cubit.addBank(context);
                          }
                        },
                        child: const Text('Add Account'),
                      ),
                      const SizedBox(height: 16),

                      // Display Result
                      Text(
                        cubit.result,
                        style: const TextStyle(fontSize: 16),
                        textAlign: TextAlign.center,
                      ),
                    ],
                  ),
                ),
              ),
            ),
          );
        },
      ),
    );
  }
}
