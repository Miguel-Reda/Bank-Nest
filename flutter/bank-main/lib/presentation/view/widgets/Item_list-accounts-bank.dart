import 'dart:async';

import 'package:bank/data/models/add_bank_model.dart';
import 'package:bank/presentation/view_model/cubit/bank_cubit.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:intl/intl.dart';

Widget itemListAccountsBank(
    {required AddBankModel model,
    required BuildContext context,
    required int index}) {
  DateTime selectedDateTime = DateTime.now();
  TimeOfDay selectedTime = TimeOfDay.now();

  Future<void> _selectDateTime() async {
    // Show date picker restricted to dates after account creation
    final DateTime? pickedDate = await showDatePicker(
      context: context,
      initialDate: selectedDateTime,
      firstDate: model.createAt ??
          DateTime(2000), // Account creation date is the minimum date
      lastDate: DateTime(2101),
    );

    if (pickedDate != null) {
      // Show time picker
      final TimeOfDay? pickedTime = await showTimePicker(
        context: context,
        initialTime: selectedTime,
      );

      if (pickedTime != null) {
        // Combine the picked date and time
        selectedDateTime = DateTime(
          pickedDate.year,
          pickedDate.month,
          pickedDate.day,
          pickedTime.hour,
          pickedTime.minute,
          DateTime.now().second,
        );

        // Validate if the selected date is before the account creation date
        if (selectedDateTime.isBefore(model.createAt!)) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text(
                'Cannot select a date before the creation date!',
              ),
              backgroundColor: Colors.red,
            ),
          );
        } else {
          print('Selected DateTime: $selectedDateTime');
        }
      }
    }
  }

  return Container(
    decoration: BoxDecoration(
      borderRadius: BorderRadius.circular(20),
      color: Colors.white,
      border: Border.all(color: Colors.black),
    ),
    child: Padding(
      padding: const EdgeInsets.all(10),
      child: Column(
        children: [
          Row(
            children: [
              const Spacer(),
              IconButton(
                onPressed: () {
                  context.read<BankCubit>().deleteBank(index);
                },
                icon: const Icon(
                  Icons.delete,
                  color: Colors.red,
                ),
              )
            ],
          ),
          Row(
            children: [
              const Text(
                'Bank Name : ',
                style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.bold,
                    color: Colors.grey),
              ),
              const Spacer(),
              Text(
                model.bankName,
                style:
                    const TextStyle(fontSize: 12, fontWeight: FontWeight.w600),
              )
            ],
          ),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 12.0),
            child: Container(
              height: 1,
              width: double.infinity,
              color: Colors.grey,
            ),
          ),
          Row(
            children: [
              const Text(
                'Amount : ',
                style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.bold,
                    color: Colors.grey),
              ),
              const Spacer(),
              Text(
                model.amount,
                style:
                    const TextStyle(fontSize: 12, fontWeight: FontWeight.w600),
              )
            ],
          ),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 12.0),
            child: Container(
              height: 1,
              width: double.infinity,
              color: Colors.grey,
            ),
          ),
          Row(
            children: [
              const Text(
                'Years : ',
                style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.bold,
                    color: Colors.grey),
              ),
              const Spacer(),
              Text(
                model.year,
                style:
                    const TextStyle(fontSize: 12, fontWeight: FontWeight.w600),
              )
            ],
          ),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 12.0),
            child: Container(
              height: 1,
              width: double.infinity,
              color: Colors.grey,
            ),
          ),
          Row(
            children: [
              const Text(
                'Percentage : ',
                style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.bold,
                    color: Colors.grey),
              ),
              const Spacer(),
              Text(
                model.percent + '%',
                style:
                    const TextStyle(fontSize: 12, fontWeight: FontWeight.w600),
              )
            ],
          ),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 12.0),
            child: Container(
              height: 1,
              width: double.infinity,
              color: Colors.grey,
            ),
          ),
          Row(
            children: [
              const Text(
                'Date Created : ',
                style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.bold,
                    color: Colors.grey),
              ),
              const Spacer(),
              Text(
                DateFormat('yyyy-MM-dd – kk:mm')
                    .format(model.createAt ?? DateTime.now()),
                style:
                    const TextStyle(fontSize: 12, fontWeight: FontWeight.w600),
              )
            ],
          ),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 12.0),
            child: Container(
              height: 1,
              width: double.infinity,
              color: Colors.grey,
            ),
          ),
          SizedBox(
            height: 50,
            width: double.infinity,
            child: Column(
              children: [
                const Text(
                  'Amount Today: ',
                  style: TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.bold,
                      color: Colors.black),
                ),
                const Spacer(),
                Text(
                  model.amount,
                  style: const TextStyle(
                      fontSize: 10, fontWeight: FontWeight.bold),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 12.0),
            child: Container(
              height: 1,
              width: double.infinity,
              color: Colors.grey,
            ),
          ),
          SizedBox(
            height: 50,
            width: double.infinity,
            child: Column(
              children: [
                Text(
                  'Final Amount After ${model.year} years : ',
                  style: const TextStyle(
                      fontSize: 10,
                      fontWeight: FontWeight.bold,
                      color: Colors.black),
                ),
                const SizedBox(
                  height: 10,
                ),
                Text(
                  model.amountAfterYears.toString(),
                  style: const TextStyle(
                      fontSize: 10, fontWeight: FontWeight.bold),
                )
              ],
            ),
          ),
          ElevatedButton(
            onPressed: _selectDateTime,
            child: const Text('Amount After Unknown Date'),
          ),
          TextButton(
            onPressed: () {
              // Check that the user-selected date is valid
              if (selectedDateTime.isBefore(model.createAt!)) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text(
                        'Selected date is before the creation date! Please select a valid date.'),
                    backgroundColor: Colors.red,
                  ),
                );
              } else {
                context.read<BankCubit>().calculateFinalAmount(
                    double.parse(model.amount),
                    double.parse(model.percent),
                    model.createAt!,
                    selectedDateTime);
              }
            },
            child: Text('${context.read<BankCubit>().money.toString()}'),
          ),
        ],
      ),
    ),
  );
}
