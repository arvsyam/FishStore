package com.adl.fishstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.adl.fishstore.data.Fish
import com.adl.fishstore.repo.FishRepo
import com.adl.fishstore.repo.OnFailure
import com.adl.fishstore.repo.OnSuccess
import com.adl.fishstore.ui.theme.FishStoreTheme
import com.adl.fishstore.viewmodel.FishViewModel
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : ComponentActivity() {
    val fishViewModel by viewModels<FishViewModel>(factoryProducer = { FishViewModelFactory(
        FishRepo()
    ) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FishStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FishShow(fishViewModel = fishViewModel)
                }
            }
        }
    }
}

@Composable
fun FishItem(fish: Fish){
    var showFishFullDescription by remember { mutableStateOf(false) }


    Column(modifier = Modifier.clickable {
        showFishFullDescription = showFishFullDescription.not()

    }) {

        Row(modifier = Modifier.padding(12.dp)){

            AsyncImage(
                model = fish.image,

                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit
            )

            Column(modifier = Modifier.padding(5.dp,1.dp,0.dp,0.dp)) {
                Text(text = fish.productName, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                Text(text = fish.category, style = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp))
                Text(text = fish.description, style = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp))
            }
            Button(modifier = Modifier.padding(20.dp,1.dp,0.dp,10.dp),onClick ={},content={ Image(painter = painterResource(id= R.drawable.ic_dropdown), modifier =Modifier.size(20.dp),contentScale = ContentScale.Fit, contentDescription = "")})
        }
        AnimatedVisibility(visible = showFishFullDescription) {
            Text(text = fish.fullDescription,
                style = TextStyle(fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic, fontSize = 11.sp),
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp))
        }


    }
}

@Composable
fun FishShow(
    fishViewModel: FishViewModel
){

    when(val fishList = fishViewModel.fishStateFlow.asStateFlow().collectAsState().value){
        is OnFailure ->{

        }
        is OnSuccess ->{
            val listOfFish = fishList.querySnapshot?.toObjects(Fish::class.java)
            listOfFish?.let{
                Column() {
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)){
                        items(listOfFish){
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                                shape = RoundedCornerShape(16.dp), elevation = 5.dp){
                                FishItem(it)
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun showDesc(fish:Fish){
    var showFishFullDescription by remember { mutableStateOf(false)}
    AnimatedVisibility(visible = showFishFullDescription) {
        Text(text = fish.fullDescription,
            style = TextStyle(fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic, fontSize = 11.sp),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp))
    }
}

class FishViewModelFactory (val fishRepo: FishRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FishViewModel::class.java)){
            return  FishViewModel(fishRepo) as T
        }
        throw IllegalStateException()
    }

}

