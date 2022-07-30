package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.adapter.SanPhamAdapter;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.dao.ProductTypeDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterUpdateObject;
import com.nhomduan.quanlydathang_admin.interface_.IDone;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.interface_.OnDelete;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;
import com.nhomduan.quanlydathang_admin.model.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DanhSachSanPhamByLoaiSPFragment extends Fragment implements OnClickItem, OnDelete {
    private TextView tvSoSanPham;
    private RecyclerView rcvListSPByLoai;
    private List<Product> productList;
    private SanPhamAdapter sanPhamAdapter;
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_danh_sach_san_pham_by_loai_s_p, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //code
        initView(view);
        getData();
    }

    private void initView(View view) {
        tvSoSanPham = view.findViewById(R.id.tvSoSanPham);
        rcvListSPByLoai = view.findViewById(R.id.rcvListSPByLoai);
    }

    private void getData() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            LoaiSP loaiSP = (LoaiSP) bundle.getSerializable("loai_sp");
            if(loaiSP != null) {
                setUpListSanPhamByLoai(loaiSP);
            }
        }
    }

    private void setUpListSanPhamByLoai(LoaiSP loaiSP) {
        productList = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(productList, this);
        rcvListSPByLoai.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        rcvListSPByLoai.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvListSPByLoai.setAdapter(sanPhamAdapter);
        ProductDao.getInstance().getProductByProductType(loaiSP, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                productList = (List<Product>) obj;
                sanPhamAdapter.setData(productList);
                tvSoSanPham.setText("Số sản phẩm : " + productList.size() + " sp");
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });

    }

    @Override
    public void onClickItem(Object obj) {
    }

    @Override
    public void onUpdateItem(Object obj) {
        Product product = (Product) obj;
        UpdateSanPhamFragment updateSanPhamFragment = new UpdateSanPhamFragment();
        Bundle bundle = new Bundle();
        bundle.putString("productId", product.getId());
        updateSanPhamFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, updateSanPhamFragment)
                .addToBackStack(null)
                .commit();
    }

    private static Product productNeedDelete;
    private static ProgressDialog progressDialog;
    @Override
    public void onDeleteItem(Object obj) {
        productNeedDelete = (Product) obj;
        if (productNeedDelete != null) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn có chắc chắn muốn xóa?" +
                            "\n Bạn sẽ xóa sản phẩm yêu thích của khách hàng," +
                            "\n sản phẩm trong sản phẩm trong giỏ hàng," +
                            "\n sản phẩm trong đơn hàng chưa xác nhận")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa", (dialog, i) -> {
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Đang xóa sản phẩm");
                        progressDialog.show();
                        onDelete();
                        xoaSoLuongSanPhamCuaLoai(productNeedDelete);
                    })
                    .show();

        }
    }

    private void xoaSoLuongSanPhamCuaLoai(Product product) {
        String loaiSPId = product.getLoai_sp();
        ProductTypeDao.getInstance().getProductTypeById(loaiSPId, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if(obj != null) {
                    LoaiSP loaiSP = (LoaiSP) obj;
                    loaiSP.setSoSanPhamThuocLoai(loaiSP.getSoSanPhamThuocLoai() - 1);
                    ProductTypeDao.getInstance().updateProductType(loaiSP, loaiSP.toMapSoLuongSanPham());
                }
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    private static boolean finishDeleteCart = true;
    private static boolean finishDeleteFavoriteProduct = true;
    @Override
    public synchronized void onDelete() {
        if(finishDeleteCart && finishDeleteFavoriteProduct) {
            FirebaseDatabase.getInstance().getReference().child("san_pham").child(productNeedDelete.getId())
                    .removeValue((error, ref) -> {
                        if (error == null) {
                            OverUtils.makeToast(mContext, "Xóa thành công");
                            productNeedDelete = null;
                            progressDialog.dismiss();
                        } else {
                            productNeedDelete = null;
                            progressDialog.dismiss();
                        }
                    });
        }
    }
}