
class MainViewModel @Inject constructor(
    userConfigUseCases: UserConfigUseCases
): ViewModel() {
    companion object {
        private val TAG: String? = MainViewModel::class.simpleName
    }

    // State to obtain the userConfig status stored in Datastore via delegates
    var redirectFlagState by mutableStateOf(true)

    // Initializing Start-Destination via delegates to obtain value only once
    var startDestination by mutableStateOf(Route.AppStartNavigation.route)

    /**
     * Retrieve the boolean Flag from Datastore to decide on Navigation of application
     */
    init {
        userConfigUseCases.readUserConfig().onEach { shouldStartFromHomeScreen ->
            Log.d(TAG, "init() called with shouldStartFromHomeScreen flag = $shouldStartFromHomeScreen")

            startDestination = if (shouldStartFromHomeScreen) {
                // Route to Home-Screen
                Route.HomeNavigation.route
            } else {
                // Route to OnBoarding Screen
                Route.AppStartNavigation.route
            }

            // Setting as 'false' after execution of code to exit out of SplashScreen
            delay(400)
            redirectFlagState = false
        }.launchIn(viewModelScope)      // Start collecting the flow & handle each emitted item within the coroutine scope
    }
}