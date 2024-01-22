/*
Copyright 2022 Square Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
import 'package:built_collection/built_collection.dart';
import 'package:built_value/serializer.dart';
import 'package:readersdk2/model/models.dart';

part 'serializers.g.dart';

@SerializersFor([
  Money,
  Location,
  CheckoutResult,
  TenderType,
  Tender,
  EntryMethod,
  CardDetails,
  CashDetails,
  Brand,
  Card,
  AdditionalPaymentType,
  CheckoutParameters,
  TipSettings,
  ErrorCode,
])
final Serializers serializers = _$serializers;
