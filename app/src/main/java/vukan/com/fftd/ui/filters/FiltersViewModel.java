package vukan.com.fftd.ui.filters;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vukan.com.fftd.models.PostCategory;
import vukan.com.fftd.repository.Repository;

public class FiltersViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<List<PostCategory>> mCategories;

    public FiltersViewModel() {
        repository = new Repository();
        mCategories = new MutableLiveData<>();
    }

    MutableLiveData<List<PostCategory>> getCategories() {
        mCategories = repository.getCategories();
        return mCategories;
    }
}