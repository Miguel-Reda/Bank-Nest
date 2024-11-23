import 'dart:math';

import 'package:bank/data/hive/hive_database.dart';
import 'package:bank/data/models/add_bank_model.dart';
import 'package:bank/presentation/view_model/cubit/bank_state.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

class BankCubit extends Cubit<BankState> {
  BankCubit() : super(initBankState());

  final TextEditingController amountController = TextEditingController();
  final TextEditingController yearController = TextEditingController();
  final TextEditingController percentController = TextEditingController();
  final TextEditingController bankController = TextEditingController();
  final TextEditingController creationDateController = TextEditingController();
  String result = '';
  String finalAmount = '';

  Future<void> addBank(BuildContext context) async {
    try {
      emit(loadingBankState());

      if (amountController.text.isEmpty ||
          yearController.text.isEmpty ||
          percentController.text.isEmpty ||
          bankController.text.isEmpty ||
          creationDateController.text.isEmpty) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Please fill all fields')),
        );
        return;
      }

      // Calculate final amount
      double initialAmount = double.parse(amountController.text);
      double percent = double.parse(percentController.text);
      int years = int.parse(yearController.text);

      finalAmount =
          (initialAmount * pow((1 + percent / 100), years)).toString();

      await HiveDatabase().bankBox!.add(
            AddBankModel(
              amount: amountController.text,
              finalAmount: finalAmount,
              bankName: bankController.text,
              percent: percentController.text,
              year: yearController.text,
              createAt: DateTime.parse(creationDateController.text),
              amountAfterYears: finalAmount,
            ),
          );

      emit(successBankState());
    } catch (e) {
      emit(errorBankState());
    }
  }

  Future<void> deleteBank(int index) async {
    try {
      emit(loadingDeleteBankState());
      await HiveDatabase().bankBox!.deleteAt(index);
      emit(successDeleteBankState());
    } catch (e) {
      emit(errorDeleteBankState());
    }
  }

  Future<void> editBank(
      int index, AddBankModel updatedModel, BuildContext context) async {
    try {
      emit(loadingEditBankState());

      // Calculate final amount
      double initialAmount = double.parse(updatedModel.amount);
      double percent = double.parse(updatedModel.percent);
      int years = int.parse(updatedModel.year);

      finalAmount =
          (initialAmount * pow((1 + percent / 100), years)).toString();

      await HiveDatabase().bankBox!.putAt(
            index,
            AddBankModel(
              amount: updatedModel.amount,
              finalAmount: finalAmount,
              bankName: updatedModel.bankName,
              percent: updatedModel.percent,
              year: updatedModel.year,
              createAt: updatedModel.createAt,
              amountAfterYears: finalAmount,
            ),
          );

      emit(successEditBankState());
    } catch (e) {
      emit(errorEditBankState());
    }
  }

  List<AddBankModel> get bankList {
    return HiveDatabase().bankBox!.values.toList();
  }

  // Calculate final amount dynamically
  void calcFinalAmount(
    DateTime createAt,
    double initialAmount,
    double percentPerSecond,
    int index,
    AddBankModel model,
    BuildContext context,
  ) {
    final daysPassed = DateTime.now().difference(createAt).inDays;

    final updatedAmount = initialAmount *
        pow((1 + (percentPerSecond * 60 * 24 * 365) / 100), daysPassed);

    model.finalAmount = updatedAmount.toString();

    // Optional: Automatically update the Hive entry with the new amount
    // editBank(index, model, context);

    emit(timerBankState());
  }

  double money = 0.0;
  void calculateFinalAmount(double initialAmount, double percent,
      DateTime createdAt, DateTime endDate) {
    final difference = endDate.difference(createdAt).inDays;
    money = initialAmount * pow((1 + percent / 100), difference / 365);

    print(
        'Initial amount: $initialAmount, Percent: $percent, Days passed: $difference');
    print('Final amount: $money');

    emit(AmmountafterBankState());
  }
}
