import 'package:built_value/serializer.dart';
import 'package:readersdk2/model/model.dart';
part 'serilizers.g.dart';

@SerializersFor([
  Money,
  Location,
  ErrorCode,
])
final Serializers serializers = _$serializers;
