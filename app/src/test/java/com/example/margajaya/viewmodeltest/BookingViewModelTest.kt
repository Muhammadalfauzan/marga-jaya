package com.example.margajaya.viewmodeltest

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.BookingDataModel
import com.example.margajaya.core.domain.preferences.AuthPreferences
import com.example.margajaya.core.domain.usecase.BookingUseCase
import com.example.margajaya.core.domain.usecase.notif.NotificationUseCase
import com.example.margajaya.ui.history.BookingViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class BookingViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: BookingViewModel
    private val mockBookingUseCase: BookingUseCase = mock()
    private val mockAuthPreferences: AuthPreferences = mock()
    private val mockNotificationUseCase: NotificationUseCase = mock()

    @Before
    fun setUp() {
        // Menginisialisasi ViewModel dengan mock dependencies
        viewModel = BookingViewModel(
            bookingUseCase = mockBookingUseCase,
            authPreferences = mockAuthPreferences,
            notificationUseCase = mockNotificationUseCase
        )
    }

    @Test
    fun `when token expired, should trigger session expired logic`() = runTest {
        // Mock response dari BookingUseCase untuk memicu error "Session expired"
        val flow = flowOf(Resource.Error<List<BookingDataModel>>("Session expired. Please log in again."))
        whenever(mockBookingUseCase.getAllBooking()).thenReturn(flow)

        // Memulai pengambilan data
        viewModel.getAllBookings()

        // Pastikan semua coroutine selesai sebelum verifikasi
        coroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        // Verifikasi bahwa clearUserData dipanggil
        verify(mockAuthPreferences).clearUserData()

        // Verifikasi bahwa LiveData showSessionExpiredDialog diperbarui
        assertEquals(true, viewModel.showSessionExpiredDialog.value)
    }
}
