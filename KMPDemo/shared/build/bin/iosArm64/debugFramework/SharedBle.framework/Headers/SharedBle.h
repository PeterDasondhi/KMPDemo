#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class SharedBleBleActionResultCompanion, SharedBleBleActionResult, SharedBleBleCharacteristicProperties, SharedBleBleCharacteristicInfo, SharedBleBleConnectionStatus, SharedBleBleServiceInfo, SharedBleBleConnectionState, SharedBleKotlinEnumCompanion, SharedBleKotlinEnum<E>, SharedBleKotlinArray<T>, SharedBleBleDevice, SharedBleBleRequirements, SharedBleBleExplorerUiState, SharedBleKotlinThrowable, SharedBleKotlinException, SharedBleKotlinRuntimeException, SharedBleKotlinIllegalStateException;

@protocol SharedBleKotlinComparable, SharedBleKotlinx_coroutines_coreStateFlow, SharedBleBleController, SharedBleKotlinx_coroutines_coreCoroutineScope, SharedBleKotlinIterator, SharedBleKotlinx_coroutines_coreFlowCollector, SharedBleKotlinx_coroutines_coreFlow, SharedBleKotlinx_coroutines_coreSharedFlow, SharedBleKotlinCoroutineContext, SharedBleKotlinCoroutineContextElement, SharedBleKotlinCoroutineContextKey;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

#pragma push_macro("_Nullable_result")
#if !__has_feature(nullability_nullable_result)
#undef _Nullable_result
#define _Nullable_result _Nullable
#endif

__attribute__((swift_name("KotlinBase")))
@interface SharedBleBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end

@interface SharedBleBase (SharedBleBaseCopying) <NSCopying>
@end

__attribute__((swift_name("KotlinMutableSet")))
@interface SharedBleMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end

__attribute__((swift_name("KotlinMutableDictionary")))
@interface SharedBleMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end

@interface NSError (NSErrorSharedBleKotlinException)
@property (readonly) id _Nullable kotlinException;
@end

__attribute__((swift_name("KotlinNumber")))
@interface SharedBleNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end

__attribute__((swift_name("KotlinByte")))
@interface SharedBleByte : SharedBleNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end

__attribute__((swift_name("KotlinUByte")))
@interface SharedBleUByte : SharedBleNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end

__attribute__((swift_name("KotlinShort")))
@interface SharedBleShort : SharedBleNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end

__attribute__((swift_name("KotlinUShort")))
@interface SharedBleUShort : SharedBleNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end

__attribute__((swift_name("KotlinInt")))
@interface SharedBleInt : SharedBleNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end

__attribute__((swift_name("KotlinUInt")))
@interface SharedBleUInt : SharedBleNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end

__attribute__((swift_name("KotlinLong")))
@interface SharedBleLong : SharedBleNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end

__attribute__((swift_name("KotlinULong")))
@interface SharedBleULong : SharedBleNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end

__attribute__((swift_name("KotlinFloat")))
@interface SharedBleFloat : SharedBleNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end

__attribute__((swift_name("KotlinDouble")))
@interface SharedBleDouble : SharedBleNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end

__attribute__((swift_name("KotlinBoolean")))
@interface SharedBleBoolean : SharedBleNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BleActionResult")))
@interface SharedBleBleActionResult : SharedBleBase
- (instancetype)initWithIsSuccessful:(BOOL)isSuccessful message:(NSString * _Nullable)message __attribute__((swift_name("init(isSuccessful:message:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) SharedBleBleActionResultCompanion *companion __attribute__((swift_name("companion")));
- (SharedBleBleActionResult *)doCopyIsSuccessful:(BOOL)isSuccessful message:(NSString * _Nullable)message __attribute__((swift_name("doCopy(isSuccessful:message:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL isSuccessful __attribute__((swift_name("isSuccessful")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BleActionResult.Companion")))
@interface SharedBleBleActionResultCompanion : SharedBleBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SharedBleBleActionResultCompanion *shared __attribute__((swift_name("shared")));
- (SharedBleBleActionResult *)failureMessage:(NSString *)message __attribute__((swift_name("failure(message:)")));
- (SharedBleBleActionResult *)successMessage:(NSString * _Nullable)message __attribute__((swift_name("success(message:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BleCharacteristicInfo")))
@interface SharedBleBleCharacteristicInfo : SharedBleBase
- (instancetype)initWithUuid:(NSString *)uuid properties:(SharedBleBleCharacteristicProperties *)properties lastValueHex:(NSString * _Nullable)lastValueHex __attribute__((swift_name("init(uuid:properties:lastValueHex:)"))) __attribute__((objc_designated_initializer));
- (SharedBleBleCharacteristicInfo *)doCopyUuid:(NSString *)uuid properties:(SharedBleBleCharacteristicProperties *)properties lastValueHex:(NSString * _Nullable)lastValueHex __attribute__((swift_name("doCopy(uuid:properties:lastValueHex:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable lastValueHex __attribute__((swift_name("lastValueHex")));
@property (readonly) SharedBleBleCharacteristicProperties *properties __attribute__((swift_name("properties")));
@property (readonly) NSString *uuid __attribute__((swift_name("uuid")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BleCharacteristicProperties")))
@interface SharedBleBleCharacteristicProperties : SharedBleBase
- (instancetype)initWithIsReadable:(BOOL)isReadable isWritable:(BOOL)isWritable isNotifiable:(BOOL)isNotifiable __attribute__((swift_name("init(isReadable:isWritable:isNotifiable:)"))) __attribute__((objc_designated_initializer));
- (SharedBleBleCharacteristicProperties *)doCopyIsReadable:(BOOL)isReadable isWritable:(BOOL)isWritable isNotifiable:(BOOL)isNotifiable __attribute__((swift_name("doCopy(isReadable:isWritable:isNotifiable:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL isNotifiable __attribute__((swift_name("isNotifiable")));
@property (readonly) BOOL isReadable __attribute__((swift_name("isReadable")));
@property (readonly) BOOL isWritable __attribute__((swift_name("isWritable")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BleConnectionState")))
@interface SharedBleBleConnectionState : SharedBleBase
- (instancetype)initWithStatus:(SharedBleBleConnectionStatus *)status connectedDeviceAddress:(NSString * _Nullable)connectedDeviceAddress connectedDeviceName:(NSString * _Nullable)connectedDeviceName services:(NSArray<SharedBleBleServiceInfo *> *)services errorMessage:(NSString * _Nullable)errorMessage __attribute__((swift_name("init(status:connectedDeviceAddress:connectedDeviceName:services:errorMessage:)"))) __attribute__((objc_designated_initializer));
- (SharedBleBleConnectionState *)doCopyStatus:(SharedBleBleConnectionStatus *)status connectedDeviceAddress:(NSString * _Nullable)connectedDeviceAddress connectedDeviceName:(NSString * _Nullable)connectedDeviceName services:(NSArray<SharedBleBleServiceInfo *> *)services errorMessage:(NSString * _Nullable)errorMessage __attribute__((swift_name("doCopy(status:connectedDeviceAddress:connectedDeviceName:services:errorMessage:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable connectedDeviceAddress __attribute__((swift_name("connectedDeviceAddress")));
@property (readonly) NSString * _Nullable connectedDeviceName __attribute__((swift_name("connectedDeviceName")));
@property (readonly) NSString * _Nullable errorMessage __attribute__((swift_name("errorMessage")));
@property (readonly) NSArray<SharedBleBleServiceInfo *> *services __attribute__((swift_name("services")));
@property (readonly) SharedBleBleConnectionStatus *status __attribute__((swift_name("status")));
@end

__attribute__((swift_name("KotlinComparable")))
@protocol SharedBleKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end

__attribute__((swift_name("KotlinEnum")))
@interface SharedBleKotlinEnum<E> : SharedBleBase <SharedBleKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) SharedBleKotlinEnumCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BleConnectionStatus")))
@interface SharedBleBleConnectionStatus : SharedBleKotlinEnum<SharedBleBleConnectionStatus *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) SharedBleBleConnectionStatus *disconnected __attribute__((swift_name("disconnected")));
@property (class, readonly) SharedBleBleConnectionStatus *connecting __attribute__((swift_name("connecting")));
@property (class, readonly) SharedBleBleConnectionStatus *discoveringServices __attribute__((swift_name("discoveringServices")));
@property (class, readonly) SharedBleBleConnectionStatus *connected __attribute__((swift_name("connected")));
@property (class, readonly) SharedBleBleConnectionStatus *failed __attribute__((swift_name("failed")));
+ (SharedBleKotlinArray<SharedBleBleConnectionStatus *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<SharedBleBleConnectionStatus *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((swift_name("BleController")))
@protocol SharedBleBleController
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)connectDeviceAddress:(NSString *)deviceAddress completionHandler:(void (^)(SharedBleBleActionResult * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("connect(deviceAddress:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)disconnectWithCompletionHandler:(void (^)(SharedBleBleActionResult * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("disconnect(completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)readCharacteristicServiceUuid:(NSString *)serviceUuid characteristicUuid:(NSString *)characteristicUuid completionHandler:(void (^)(SharedBleBleActionResult * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("readCharacteristic(serviceUuid:characteristicUuid:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)startScanWithCompletionHandler:(void (^)(SharedBleBleActionResult * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("startScan(completionHandler:)")));
- (void)stopScan __attribute__((swift_name("stopScan()")));
@property (readonly) id<SharedBleKotlinx_coroutines_coreStateFlow> connectionState __attribute__((swift_name("connectionState")));
@property (readonly) id<SharedBleKotlinx_coroutines_coreStateFlow> scannedDevices __attribute__((swift_name("scannedDevices")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BleDevice")))
@interface SharedBleBleDevice : SharedBleBase
- (instancetype)initWithAddress:(NSString *)address name:(NSString * _Nullable)name rssi:(int32_t)rssi __attribute__((swift_name("init(address:name:rssi:)"))) __attribute__((objc_designated_initializer));
- (SharedBleBleDevice *)doCopyAddress:(NSString *)address name:(NSString * _Nullable)name rssi:(int32_t)rssi __attribute__((swift_name("doCopy(address:name:rssi:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *address __attribute__((swift_name("address")));
@property (readonly) NSString * _Nullable name __attribute__((swift_name("name")));
@property (readonly) int32_t rssi __attribute__((swift_name("rssi")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BleExplorerHost")))
@interface SharedBleBleExplorerHost : SharedBleBase
- (instancetype)initWithBleController:(id<SharedBleBleController>)bleController scope:(id<SharedBleKotlinx_coroutines_coreCoroutineScope>)scope __attribute__((swift_name("init(bleController:scope:)"))) __attribute__((objc_designated_initializer));
- (void)clearMessage __attribute__((swift_name("clearMessage()")));
- (void)stopScan __attribute__((swift_name("stopScan()")));
- (void)updateSystemStateHasBluetoothPermissions:(BOOL)hasBluetoothPermissions isBluetoothEnabled:(BOOL)isBluetoothEnabled __attribute__((swift_name("updateSystemState(hasBluetoothPermissions:isBluetoothEnabled:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BleExplorerPresenter")))
@interface SharedBleBleExplorerPresenter : SharedBleBase
- (instancetype)initWithBleController:(id<SharedBleBleController>)bleController scope:(id<SharedBleKotlinx_coroutines_coreCoroutineScope>)scope __attribute__((swift_name("init(bleController:scope:)"))) __attribute__((objc_designated_initializer));
- (void)clearMessage __attribute__((swift_name("clearMessage()")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)connectDeviceAddress:(NSString *)deviceAddress completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("connect(deviceAddress:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)disconnectWithCompletionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("disconnect(completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)readCharacteristicServiceUuid:(NSString *)serviceUuid characteristicUuid:(NSString *)characteristicUuid completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("readCharacteristic(serviceUuid:characteristicUuid:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)startScanWithCompletionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("startScan(completionHandler:)")));
- (void)stopScan __attribute__((swift_name("stopScan()")));
- (void)updateSystemStateHasBluetoothPermissions:(BOOL)hasBluetoothPermissions isBluetoothEnabled:(BOOL)isBluetoothEnabled __attribute__((swift_name("updateSystemState(hasBluetoothPermissions:isBluetoothEnabled:)")));
@property (readonly) id<SharedBleKotlinx_coroutines_coreStateFlow> uiState __attribute__((swift_name("uiState")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BleExplorerUiState")))
@interface SharedBleBleExplorerUiState : SharedBleBase
- (instancetype)initWithRequirements:(SharedBleBleRequirements *)requirements isScanning:(BOOL)isScanning devices:(NSArray<SharedBleBleDevice *> *)devices connectionState:(SharedBleBleConnectionState *)connectionState bannerMessage:(NSString * _Nullable)bannerMessage __attribute__((swift_name("init(requirements:isScanning:devices:connectionState:bannerMessage:)"))) __attribute__((objc_designated_initializer));
- (SharedBleBleExplorerUiState *)doCopyRequirements:(SharedBleBleRequirements *)requirements isScanning:(BOOL)isScanning devices:(NSArray<SharedBleBleDevice *> *)devices connectionState:(SharedBleBleConnectionState *)connectionState bannerMessage:(NSString * _Nullable)bannerMessage __attribute__((swift_name("doCopy(requirements:isScanning:devices:connectionState:bannerMessage:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable bannerMessage __attribute__((swift_name("bannerMessage")));
@property (readonly) BOOL canConnect __attribute__((swift_name("canConnect")));
@property (readonly) BOOL canStartScan __attribute__((swift_name("canStartScan")));
@property (readonly) SharedBleBleConnectionState *connectionState __attribute__((swift_name("connectionState")));
@property (readonly) NSArray<SharedBleBleDevice *> *devices __attribute__((swift_name("devices")));
@property (readonly) BOOL isScanning __attribute__((swift_name("isScanning")));
@property (readonly) SharedBleBleRequirements *requirements __attribute__((swift_name("requirements")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BleRequirements")))
@interface SharedBleBleRequirements : SharedBleBase
- (instancetype)initWithHasBluetoothPermissions:(BOOL)hasBluetoothPermissions isBluetoothEnabled:(BOOL)isBluetoothEnabled __attribute__((swift_name("init(hasBluetoothPermissions:isBluetoothEnabled:)"))) __attribute__((objc_designated_initializer));
- (SharedBleBleRequirements *)doCopyHasBluetoothPermissions:(BOOL)hasBluetoothPermissions isBluetoothEnabled:(BOOL)isBluetoothEnabled __attribute__((swift_name("doCopy(hasBluetoothPermissions:isBluetoothEnabled:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) BOOL hasBluetoothPermissions __attribute__((swift_name("hasBluetoothPermissions")));
@property (readonly) BOOL isBluetoothEnabled __attribute__((swift_name("isBluetoothEnabled")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("BleServiceInfo")))
@interface SharedBleBleServiceInfo : SharedBleBase
- (instancetype)initWithUuid:(NSString *)uuid characteristics:(NSArray<SharedBleBleCharacteristicInfo *> *)characteristics __attribute__((swift_name("init(uuid:characteristics:)"))) __attribute__((objc_designated_initializer));
- (SharedBleBleServiceInfo *)doCopyUuid:(NSString *)uuid characteristics:(NSArray<SharedBleBleCharacteristicInfo *> *)characteristics __attribute__((swift_name("doCopy(uuid:characteristics:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<SharedBleBleCharacteristicInfo *> *characteristics __attribute__((swift_name("characteristics")));
@property (readonly) NSString *uuid __attribute__((swift_name("uuid")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IosBleController")))
@interface SharedBleIosBleController : SharedBleBase <SharedBleBleController>
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)connectDeviceAddress:(NSString *)deviceAddress completionHandler:(void (^)(SharedBleBleActionResult * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("connect(deviceAddress:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)disconnectWithCompletionHandler:(void (^)(SharedBleBleActionResult * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("disconnect(completionHandler:)")));
- (BOOL)hasBluetoothPermission __attribute__((swift_name("hasBluetoothPermission()")));
- (BOOL)isBluetoothEnabled __attribute__((swift_name("isBluetoothEnabled()")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)readCharacteristicServiceUuid:(NSString *)serviceUuid characteristicUuid:(NSString *)characteristicUuid completionHandler:(void (^)(SharedBleBleActionResult * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("readCharacteristic(serviceUuid:characteristicUuid:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)startScanWithCompletionHandler:(void (^)(SharedBleBleActionResult * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("startScan(completionHandler:)")));
- (void)stopScan __attribute__((swift_name("stopScan()")));
@property (readonly) id<SharedBleKotlinx_coroutines_coreStateFlow> connectionState __attribute__((swift_name("connectionState")));
@property (readonly) id<SharedBleKotlinx_coroutines_coreStateFlow> scannedDevices __attribute__((swift_name("scannedDevices")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IosBleExplorerHost")))
@interface SharedBleIosBleExplorerHost : SharedBleBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (void)clearMessage __attribute__((swift_name("clearMessage()")));
- (void)dispose __attribute__((swift_name("dispose()")));
- (SharedBleBleExplorerUiState *)getCurrentUiState __attribute__((swift_name("getCurrentUiState()")));
- (void)refreshSystemState __attribute__((swift_name("refreshSystemState()")));
- (void)requestConnectDeviceAddress:(NSString *)deviceAddress __attribute__((swift_name("requestConnect(deviceAddress:)")));
- (void)requestDisconnect __attribute__((swift_name("requestDisconnect()")));
- (void)requestReadCharacteristicServiceUuid:(NSString *)serviceUuid characteristicUuid:(NSString *)characteristicUuid __attribute__((swift_name("requestReadCharacteristic(serviceUuid:characteristicUuid:)")));
- (void)requestStartScan __attribute__((swift_name("requestStartScan()")));
- (void (^)(void))startObservingUiStateOnUpdate:(void (^)(SharedBleBleExplorerUiState *))onUpdate __attribute__((swift_name("startObservingUiState(onUpdate:)")));
- (void)stopScan __attribute__((swift_name("stopScan()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinEnumCompanion")))
@interface SharedBleKotlinEnumCompanion : SharedBleBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) SharedBleKotlinEnumCompanion *shared __attribute__((swift_name("shared")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface SharedBleKotlinArray<T> : SharedBleBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(SharedBleInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<SharedBleKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("KotlinThrowable")))
@interface SharedBleKotlinThrowable : SharedBleBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(SharedBleKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(SharedBleKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));

/**
 * @note annotations
 *   kotlin.experimental.ExperimentalNativeApi
*/
- (SharedBleKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) SharedBleKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (NSError *)asError __attribute__((swift_name("asError()")));
@end

__attribute__((swift_name("KotlinException")))
@interface SharedBleKotlinException : SharedBleKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(SharedBleKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(SharedBleKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinRuntimeException")))
@interface SharedBleKotlinRuntimeException : SharedBleKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(SharedBleKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(SharedBleKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinIllegalStateException")))
@interface SharedBleKotlinIllegalStateException : SharedBleKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(SharedBleKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(SharedBleKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.4")
*/
__attribute__((swift_name("KotlinCancellationException")))
@interface SharedBleKotlinCancellationException : SharedBleKotlinIllegalStateException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(SharedBleKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(SharedBleKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreFlow")))
@protocol SharedBleKotlinx_coroutines_coreFlow
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<SharedBleKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreSharedFlow")))
@protocol SharedBleKotlinx_coroutines_coreSharedFlow <SharedBleKotlinx_coroutines_coreFlow>
@required
@property (readonly) NSArray<id> *replayCache __attribute__((swift_name("replayCache")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreStateFlow")))
@protocol SharedBleKotlinx_coroutines_coreStateFlow <SharedBleKotlinx_coroutines_coreSharedFlow>
@required
@property (readonly) id _Nullable value __attribute__((swift_name("value")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreCoroutineScope")))
@protocol SharedBleKotlinx_coroutines_coreCoroutineScope
@required
@property (readonly) id<SharedBleKotlinCoroutineContext> coroutineContext __attribute__((swift_name("coroutineContext")));
@end

__attribute__((swift_name("KotlinIterator")))
@protocol SharedBleKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreFlowCollector")))
@protocol SharedBleKotlinx_coroutines_coreFlowCollector
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)emitValue:(id _Nullable)value completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("emit(value:completionHandler:)")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.3")
*/
__attribute__((swift_name("KotlinCoroutineContext")))
@protocol SharedBleKotlinCoroutineContext
@required
- (id _Nullable)foldInitial:(id _Nullable)initial operation:(id _Nullable (^)(id _Nullable, id<SharedBleKotlinCoroutineContextElement>))operation __attribute__((swift_name("fold(initial:operation:)")));
- (id<SharedBleKotlinCoroutineContextElement> _Nullable)getKey:(id<SharedBleKotlinCoroutineContextKey>)key __attribute__((swift_name("get(key:)")));
- (id<SharedBleKotlinCoroutineContext>)minusKeyKey:(id<SharedBleKotlinCoroutineContextKey>)key __attribute__((swift_name("minusKey(key:)")));
- (id<SharedBleKotlinCoroutineContext>)plusContext:(id<SharedBleKotlinCoroutineContext>)context __attribute__((swift_name("plus(context:)")));
@end

__attribute__((swift_name("KotlinCoroutineContextElement")))
@protocol SharedBleKotlinCoroutineContextElement <SharedBleKotlinCoroutineContext>
@required
@property (readonly) id<SharedBleKotlinCoroutineContextKey> key __attribute__((swift_name("key")));
@end

__attribute__((swift_name("KotlinCoroutineContextKey")))
@protocol SharedBleKotlinCoroutineContextKey
@required
@end

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
