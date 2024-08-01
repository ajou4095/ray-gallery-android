package com.ray.gallery.android.presentation.ui.main.home

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.ray.gallery.android.common.util.coroutine.event.MutableEventFlow
import com.ray.gallery.android.common.util.coroutine.event.eventObserve
import com.ray.gallery.android.presentation.R
import com.ray.gallery.android.presentation.common.theme.Black
import com.ray.gallery.android.presentation.common.theme.Blue900
import com.ray.gallery.android.presentation.common.theme.Body0
import com.ray.gallery.android.presentation.common.theme.Gray100
import com.ray.gallery.android.presentation.common.theme.Gray200
import com.ray.gallery.android.presentation.common.theme.Gray400
import com.ray.gallery.android.presentation.common.theme.Gray900
import com.ray.gallery.android.presentation.common.theme.Red900
import com.ray.gallery.android.presentation.common.theme.Space16
import com.ray.gallery.android.presentation.common.theme.Space20
import com.ray.gallery.android.presentation.common.theme.Space24
import com.ray.gallery.android.presentation.common.theme.Space4
import com.ray.gallery.android.presentation.common.theme.Space56
import com.ray.gallery.android.presentation.common.theme.Space8
import com.ray.gallery.android.presentation.common.theme.White
import com.ray.gallery.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import com.ray.gallery.android.presentation.common.view.RippleBox
import com.ray.gallery.android.presentation.common.view.dropdown.TextDropdownMenu
import com.ray.gallery.android.presentation.model.FolderModel
import com.ray.gallery.android.presentation.model.ImageModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.plus

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    argument: HomeArgument,
    data: HomeData
) {
    val (state, event, intent, logEvent, handler) = argument
    val scope = rememberCoroutineScope() + handler
    val localConfiguration = LocalConfiguration.current

    val perMissionAlbumLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            intent(HomeIntent.OnGrantPermission)
        } else {
//            onDismissRequest()
        }
    }
    val selectedList: MutableList<ImageModel> = remember {
        mutableStateListOf<ImageModel>()
    }
    var isDropDownMenuExpanded: Boolean by remember { mutableStateOf(false) }
    var currentFolder: FolderModel by remember { mutableStateOf(FolderModel.recent) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        val (topBar, contents) = createRefs()

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(contents) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            columns = GridCells.Adaptive(minSize = 128.dp),
            contentPadding = PaddingValues(top = Space56),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            // TODO : 아무것도 없을 경우 처리
            items(data.imageModelList.itemCount) { index ->
                data.imageModelList[index]?.let { gallery ->
                    HomeScreenItem(
                        galleryImage = gallery,
                        selectedList = selectedList,
                        onClickImage = { image ->

                        },
                        onSelectImage = { image ->
                            selectedList.add(image)
                        },
                        onDeleteImage = { image ->
                            selectedList.removeIf { it.id == image.id }
                        }
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .height(Space56)
                .background(White.copy(alpha = 0.9f))
                .constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(Space20))
            RippleBox(
                onClick = {
//                    onDismissRequest()
                }
            ) {
                Icon(
                    modifier = Modifier.size(Space24),
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    tint = Gray900
                )
            }
            Spacer(modifier = Modifier.width(Space20))
            RippleBox(
                onClick = {
                    isDropDownMenuExpanded = true
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentFolder.name,
                        style = Body0.merge(Gray900)
                    )
                    Icon(
                        modifier = Modifier.size(Space16),
                        painter = painterResource(R.drawable.ic_chevron_down),
                        contentDescription = null,
                        tint = Gray900
                    )
                }
                TextDropdownMenu(
                    items = data.folderList,
                    label = { it.name },
                    isExpanded = isDropDownMenuExpanded,
                    onDismissRequest = { isDropDownMenuExpanded = false },
                    onClick = { folder ->
                        currentFolder = folder
                        intent(HomeIntent.OnChangeFolder(folder))
                    }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            RippleBox(
                onClick = {
//                    onDismissRequest()
                }
            ) {
                Icon(
                    modifier = Modifier.size(Space24),
                    painter = painterResource(id = R.drawable.ic_more_vertical),
                    contentDescription = null,
                    tint = Gray900
                )
            }
            Spacer(modifier = Modifier.width(Space20))
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            perMissionAlbumLauncher.launch(
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            perMissionAlbumLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    LaunchedEffectWithLifecycle(event, handler) {
        event.eventObserve { event ->

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenItem(
    galleryImage: ImageModel,
    selectedList: List<ImageModel>,
    onClickImage: (ImageModel) -> Unit,
    onSelectImage: (ImageModel) -> Unit,
    onDeleteImage: (ImageModel) -> Unit
) {
    val context = LocalContext.current

    val index = selectedList.indexOfFirst { it.id == galleryImage.id }

    Box(
        modifier = Modifier
            .background(Gray200)
            .combinedClickable(
                onClick = {
                    if (index > -1) {
                        onDeleteImage(galleryImage)
                    } else {
                        onClickImage(galleryImage)
                    }
                },
                onLongClick = { onSelectImage(galleryImage) }
            )
            .run {
                if (index > -1) {
                    border(BorderStroke(2.dp, Blue900))
                } else {
                    this
                }
            }
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(galleryImage.filePath)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(1f)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(Space24)
                            .align(Alignment.Center),
                        color = Gray100,
                        strokeWidth = 2.dp
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        modifier = Modifier
                            .size(Space24)
                            .align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = null,
                        tint = Red900
                    )
                }
            }
        )

        if (index > -1) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .align(Alignment.Center)
                    .background(Black.copy(alpha = 0.4f))
            )
        }

        if (index > -1) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Space8)
            ) {
                Box(
                    modifier = Modifier
                        .size(Space24)
                        .clip(CircleShape)
                        .background(Blue900, CircleShape)
                ) {
                    Text(
                        text = (index + 1).toString(),
                        modifier = Modifier.align(Alignment.Center),
                        style = Body0.merge(White)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Space8)
            ) {
                Box(
                    modifier = Modifier
                        .size(Space24)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(White.copy(alpha = 0.6f), CircleShape)
                        .border(1.dp, Gray400, CircleShape)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Black.copy(alpha = 0.6f))
        ) {
            Box(
                modifier = Modifier.padding(Space4)
            ) {
                Text(
                    text = galleryImage.name,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = Body0.merge(White)
                )
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun HomeScreenPreview1() {
    HomeScreen(
        navController = rememberNavController(),
        argument = HomeArgument(
            state = HomeState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            handler = CoroutineExceptionHandler { _, _ -> }
        ),
        data = HomeData(
            folderList = listOf(),
            imageModelList = MutableStateFlow<PagingData<ImageModel>>(PagingData.empty()).collectAsLazyPagingItems(),
        )
    )
}

@Preview(apiLevel = 34)
@Composable
private fun HomeScreenPreview2() {
    HomeScreen(
        navController = rememberNavController(),
        argument = HomeArgument(
            state = HomeState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            handler = CoroutineExceptionHandler { _, _ -> }
        ),
        data = HomeData(
            folderList = listOf(),
            imageModelList = MutableStateFlow<PagingData<ImageModel>>(
                PagingData.from(
                    listOf(
                        ImageModel(
                            id = 1,
                            filePath = "https://via.placeholder.com/150",
                            name = "image1",
                            date = "2021-09-01"
                        ),
                        ImageModel(
                            id = 2,
                            filePath = "https://via.placeholder.com/150",
                            name = "image1",
                            date = "2021-09-01"
                        )
                    )
                )
            ).collectAsLazyPagingItems()
        )
    )
}
