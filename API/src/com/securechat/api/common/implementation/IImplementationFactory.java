package com.securechat.api.common.implementation;

import java.util.Map;
import java.util.function.Supplier;

public interface IImplementationFactory {

	public void inject(Object obj);

	public <T> T get(Class<T> type, boolean provide);

	public void set(Class<?> type, Object instance);

	public <T extends IImplementation> IImplementationInstance<T> registerInstance(ImplementationMarker marker,
			Class<T> type, T inst);

	public <T extends IImplementation> IImplementationInstance<T> register(ImplementationMarker marker, Class<T> type,
			Supplier<? extends T> supplier);

	public <T extends IImplementation> IImplementationInstance<T> register(ImplementationMarker marker, Class<T> type,
			Supplier<? extends T> supplier, boolean inject);

	public <T extends IImplementation> T provide(Class<T> type);

	public <T extends IImplementation> T provide(Class<T> type, ImplementationMarker marker);

	public <T extends IImplementation> T provide(Class<T> type, ImplementationMarker[] providers, boolean allowDefault,
			boolean associate, String associateName);

	public <T extends IImplementation> ImplementationMarker getProvider(Class<T> type, ImplementationMarker[] providers,
			boolean allowDefault, boolean associate, String associateName);

	public <T extends IImplementation> Map<ImplementationMarker, IImplementationInstance<? extends T>> getImplementations(
			Class<T> type);

	public <T extends IImplementation> boolean doesProviderExist(Class<T> type, ImplementationMarker marker);

	public <T extends IImplementation> void setFallbackDefaultIfNone(Class<T> type, ImplementationMarker defaultMarker);

	public <T extends IImplementation> void setFallbackDefault(Class<T> type, ImplementationMarker defaultMarker);

	public void flushDefaults();

	public <T extends IImplementation> ImplementationMarker getDefault(Class<T> type);

}
